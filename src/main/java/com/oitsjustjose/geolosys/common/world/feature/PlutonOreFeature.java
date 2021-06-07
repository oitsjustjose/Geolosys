package com.oitsjustjose.geolosys.common.world.feature;

import java.util.HashSet;
import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.ChunkPosDim;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.PlutonType;
import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.SampleUtils;
import com.oitsjustjose.geolosys.common.world.capability.IGeolosysCapability;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PlutonOreFeature extends Feature<NoFeatureConfig> {
    public PlutonOreFeature(Codec<NoFeatureConfig> p_i231976_1_) {
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
        if (rand.nextInt(CommonConfig.CHUNK_SKIP_CHANCE.get()) > pluton.getChance()) {
            return false;
        }

        // Logic to confirm that this can be placed here
        if (pluton instanceof DepositBiomeRestricted) {
            DepositBiomeRestricted restricted = (DepositBiomeRestricted) pluton;
            if (!restricted.canPlaceInBiome(reader.getBiome(pos))) {
                return false;
            }
        } else if (pluton instanceof DepositMultiOreBiomeRestricted) {
            DepositMultiOreBiomeRestricted restricted = (DepositMultiOreBiomeRestricted) pluton;
            if (!restricted.canPlaceInBiome(reader.getBiome(pos))) {
                return false;
            }
        }

        for (String s : pluton.getDimensionFilter()) {
            boolean a = pluton.isDimensionFilterBlacklist();
            boolean b = dimName.equals(new ResourceLocation(s).toString());

            /*
             * If dim blacklist and the current dim is in the BL, cancel OR if dim whitelist
             * and the current dim is NOT in the BL, cancel
             */
            if ((a && b) || (!a && !b)) {
                return false;
            }
        }

        boolean anyGenerated = false;

        switch (pluton.getPlutonType()) {
            case DENSE:
                anyGenerated = FeatureUtils.generateDense(reader, pos, rand, pluton, plutonCapability);
                this.postPlacement(reader, pos, pluton);
                break;
            case SPARSE:
                anyGenerated = FeatureUtils.generateSparse(reader, pos, rand, pluton, plutonCapability);
                this.postPlacement(reader, pos, pluton);
                break;
            case DIKE:
                anyGenerated = FeatureUtils.generateDike(reader, pos, rand, pluton, plutonCapability);
                this.postPlacement(reader, pos, pluton);
                break;
            case LAYER:
                anyGenerated = FeatureUtils.generateLayer(reader, pos, rand, pluton, plutonCapability);
                this.postPlacement(reader, pos, pluton);
                break;
            case TOP_LAYER:
                anyGenerated = FeatureUtils.generateTopLayer(reader, pos, rand, pluton, plutonCapability);
                break;
        }

        if (anyGenerated) {
            if (pluton.getPlutonType() == PlutonType.TOP_LAYER && rand.nextInt(10) == 0) {
                return generate(reader, generator, rand, pos, config);
            }
            plutonCapability.setOrePlutonGenerated(new ChunkPosDim(pos.getX(), pos.getZ(), dimName));
            return true;
        }

        return false;
    }

    /**
     * Post-pluton-placement logic to generate samples and more
     *
     * @param reader         an ISeedReader instance
     * @param plutonStartPos The BlockPos from which the pluton originated
     * @param ore            The IDeposit that was generated
     */
    private void postPlacement(ISeedReader reader, BlockPos plutonStartPos, IDeposit ore) {
        if (CommonConfig.DEBUG_WORLD_GEN.get()) {
            StringBuilder sb = new StringBuilder();
            String plutonSummary = "";

            if (ore instanceof DepositMultiOre) {
                DepositMultiOre d = (DepositMultiOre) ore;
                HashSet<BlockState> unique = new HashSet<BlockState>();

                d.getOres().forEach((e) -> unique.add(e));
                unique.forEach((state) -> {
                    sb.append(state.getBlock().getRegistryName().toString());
                    sb.append(", ");
                });
                // Convert to string, remove straggling ", "
                plutonSummary = sb.toString();
                plutonSummary = plutonSummary.substring(0, plutonSummary.length() - 2);
            } else {
                plutonSummary = ore.getOre().getBlock().getRegistryName().toString();
            }

            Geolosys.getInstance().LOGGER.debug("Generated {} in Chunk {} (Pos [{} {} {}])", plutonSummary,
                    new ChunkPos(plutonStartPos), plutonStartPos.getX(), plutonStartPos.getY(), plutonStartPos.getZ());
        }

        int sampleLimit = SampleUtils.getSampleCount(ore);
        for (int i = 0; i < sampleLimit; i++) {
            BlockPos samplePos = SampleUtils.getSamplePosition(reader, new ChunkPos(plutonStartPos), ore.getYMax());
            if (samplePos == null || SampleUtils.inNonWaterFluid(reader, samplePos)) {
                continue;
            }

            if (reader.getBlockState(samplePos) != ore.getSampleBlock()) {
                if (SampleUtils.isInWater(reader, samplePos)
                        && ore.getSampleBlock().hasProperty(BlockStateProperties.WATERLOGGED)) {
                    reader.setBlockState(samplePos,
                            ore.getSampleBlock().with(BlockStateProperties.WATERLOGGED, Boolean.TRUE), 2 | 16);
                } else {
                    reader.setBlockState(samplePos, ore.getSampleBlock(), 2 | 16);
                }
            }
        }
    }
}