package com.oitsjustjose.geolosys.common.world.feature;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.ChunkPosDim;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.capability.IGeolosysCapability;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class DepositFeature extends Feature<NoFeatureConfig> {
    public DepositFeature(Codec<NoFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos,
            NoFeatureConfig config) {

        if (generator instanceof FlatChunkGenerator) {
            return false;
        }

        IGeolosysCapability plutonCapability = reader.getWorld().getCapability(GeolosysAPI.GEOLOSYS_WORLD_CAPABILITY)
                .orElse(null);
        if (plutonCapability == null) {
            Geolosys.getInstance().LOGGER.error("NULL PLUTON CAPABILITY!!!");
            return false;
        }

        String dimName = Utils.dimensionToString(reader);

        // Fill in pending Blocks when possible:
        plutonCapability.getPendingBlocks().forEach((pPos, pState) -> {
            if (FeatureUtils.isInChunk(new ChunkPos(pos), pPos)) {
                if (reader.setBlockState(pPos.getPos(), pState, 2 | 16)) {
                    plutonCapability.getPendingBlocks().remove(pPos);
                    if (CommonConfig.DEBUG_WORLD_GEN.get()) {
                        Geolosys.getInstance().LOGGER.info("Generated pending block "
                                + pState.getBlock().getRegistryName().toString() + " at " + pos);
                    }
                } else {
                    if (CommonConfig.DEBUG_WORLD_GEN.get()) {
                        Geolosys.getInstance().LOGGER.error("FAILED to generate pending block "
                                + pState.getBlock().getRegistryName().toString() + " at " + pos);
                    }
                }
            }
        });

        ChunkPosDim chunkPosDim = new ChunkPosDim(pos, dimName);
        if (plutonCapability.hasOrePlutonGenerated(chunkPosDim)) {
            return false;
        }

        IDeposit pluton = GeolosysAPI.plutonRegistry.pickPluton(reader, pos, rand);
        if (pluton == null) {
            return false;
        }

        // Logic for spacing out plutons
        if (rand.nextInt(CommonConfig.CHUNK_SKIP_CHANCE.get()) > pluton.getGenWt()) {
            return false;
        }

        // Logic to confirm that this can be placed here
        // if (pluton instanceof DepositBiomeRestricted) {
        //     DepositBiomeRestricted restricted = (DepositBiomeRestricted) pluton;
        //     if (!restricted.canPlaceInBiome(reader.getBiome(pos))) {
        //         return false;
        //     }
        // } else if (pluton instanceof DepositMultiOreBiomeRestricted) {
        //     DepositMultiOreBiomeRestricted restricted = (DepositMultiOreBiomeRestricted) pluton;
        //     if (!restricted.canPlaceInBiome(reader.getBiome(pos))) {
        //         return false;
        //     }
        // }

        // for (String s : pluton.getDimensionFilter()) {
        //     boolean a = pluton.isDimensionFilterBlacklist();
        //     boolean b = dimName.equals(new ResourceLocation(s).toString());

        //     /*
        //      * If dim blacklist and the current dim is in the BL, cancel OR if dim whitelist
        //      * and the current dim is NOT in the BL, cancel
        //      */
        //     if ((a && b) || (!a && !b)) {
        //         return false;
        //     }
        // }

        boolean anyGenerated = pluton.generate(reader, pos, plutonCapability) > 0;
        pluton.afterGen(reader, pos);

        // switch (pluton.getPlutonType()) {
        //     case DENSE:
        //         anyGenerated = FeatureUtils.generateDense(reader, pos, rand, pluton, plutonCapability);
        //         this.postPlacement(reader, pos, pluton);
        //         break;
        //     case SPARSE:
        //         anyGenerated = FeatureUtils.generateSparse(reader, pos, rand, pluton, plutonCapability);
        //         this.postPlacement(reader, pos, pluton);
        //         break;
        //     case DIKE:
        //         anyGenerated = FeatureUtils.generateDike(reader, pos, rand, pluton, plutonCapability);
        //         this.postPlacement(reader, pos, pluton);
        //         break;
        //     case LAYER:
        //         anyGenerated = FeatureUtils.generateLayer(reader, pos, rand, pluton, plutonCapability);
        //         this.postPlacement(reader, pos, pluton);
        //         break;
        //     case TOP_LAYER:
        //         anyGenerated = FeatureUtils.generateTopLayer(reader, pos, rand, pluton, plutonCapability);
        //         break;
        // }

        if (anyGenerated) {
            // TODO: for top layer plutons repeat about 10-ish times, see "algo" below.
            // if (pluton.getPlutonType() == PlutonType.TOP_LAYER && rand.nextInt(10) == 0) {
            //     return generate(reader, generator, rand, pos, config);
            // }
            plutonCapability.setOrePlutonGenerated(new ChunkPosDim(pos.getX(), pos.getZ(), dimName));
            return true;
        }

        return false;
    }
}