package com.oitsjustjose.geolosys.common.world.feature;

import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.ChunkPosDim;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.PlutonType;
import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.blocks.SampleBlock;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.SampleUtils;
import com.oitsjustjose.geolosys.common.world.capability.IGeolosysCapability;

import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

public class PlutonOreFeature extends Feature<NoFeatureConfig> {
    public PlutonOreFeature(Codec<NoFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    /**
     * Post-pluton-placement logic to generate samples and more
     *
     * @param world          an IWorld instance
     * @param plutonStartPos The BlockPos from which the pluton originated
     * @param ore            The IDeposit that was generated
     */
    private void postPlacement(IWorld world, BlockPos plutonStartPos, IDeposit ore) {
        if (CommonConfig.DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.debug("Generated {} in Chunk {} (Pos {})", ore.getFriendlyName(),
                    new ChunkPos(plutonStartPos), plutonStartPos);
            // Geolosys.getInstance().LOGGER.debug("Generated " +
            // + " in Chunk " + new ChunkPos(plutonStartPos));
        }

        int sampleLimit = SampleUtils.getSampleCount(ore);
        for (int i = 0; i < sampleLimit; i++) {
            BlockPos samplePos = SampleUtils.getSamplePosition(world, new ChunkPos(plutonStartPos), ore.getYMax());
            if (samplePos == null || SampleUtils.inNonWaterFluid(world, samplePos)) {
                continue;
            }

            if (world.getBlockState(samplePos) != ore.getSample()) {
                boolean isInWater = SampleUtils.isInWater(world, samplePos);
                if (ore.getSample().getBlock() instanceof SampleBlock) {
                    BlockState sampleState = isInWater ? ore.getSample().with(SampleBlock.WATERLOGGED, Boolean.TRUE)
                            : ore.getSample();
                    world.setBlockState(samplePos, sampleState, 2 | 16);
                } else {
                    // Place a waterlogged variant of whatever block it ends up being
                    if (isInWater && ore.getSample().getBlock() instanceof IWaterLoggable) {
                        world.setBlockState(samplePos,
                                ore.getSample().with(BlockStateProperties.WATERLOGGED, Boolean.TRUE), 2 | 16);
                    } else {
                        world.setBlockState(samplePos, ore.getSample(), 2 | 16);
                    }
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos,
            NoFeatureConfig config) {

        IWorld iworld = reader.getWorld();
        if (!(iworld instanceof ServerWorld)) {
            return false;
        }

        ServerWorld world = (ServerWorld) iworld;
        if (world.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator) {
            return false;
        }

        IGeolosysCapability plutonCapability = world.getCapability(GeolosysAPI.GEOLOSYS_WORLD_CAPABILITY).orElse(null);
        if (plutonCapability == null) {
            Geolosys.getInstance().LOGGER.info("NULL PLUTON CAPABILITY!!!");
            return false;
        }

        // Fill in pending Blocks when possible:
        plutonCapability.getPendingBlocks().forEach((pPos, pState) -> {
            if (FeatureUtils.isInChunk(new ChunkPos(pos), pPos)) {
                if (CommonConfig.DEBUG_WORLD_GEN.get()) {
                    Geolosys.getInstance().LOGGER.info(
                            "Generated pending block " + pState.getBlock().getRegistryName().toString() + " at " + pos);
                }
                world.setBlockState(pPos.getPos(), pState, 2 | 16);
                plutonCapability.getPendingBlocks().remove(pPos);
            }
        });

        ChunkPosDim chunkPosDim = new ChunkPosDim(pos, Utils.dimensionToString(world));
        if (plutonCapability.hasOrePlutonGenerated(chunkPosDim)) {
            return false;
        }
        IDeposit pluton = GeolosysAPI.plutonRegistry.pickPluton(world, pos, rand);
        if (pluton == null) {
            return false;
        }
        if (rand.nextInt(100) > pluton.getChance()) {
            return false;
        }
        // Logic to confirm that this can be placed here
        if (pluton instanceof DepositBiomeRestricted) {
            DepositBiomeRestricted restricted = (DepositBiomeRestricted) pluton;
            if (!restricted.canPlaceInBiome(world.getBiome(pos))) {
                return false;
            }
        } else if (pluton instanceof DepositMultiOreBiomeRestricted) {
            DepositMultiOreBiomeRestricted restricted = (DepositMultiOreBiomeRestricted) pluton;
            if (!restricted.canPlaceInBiome(world.getBiome(pos))) {
                return false;
            }
        }

        // TODO: This is the slow way -- see the 1.15 branch for the fast way
        for (String s : pluton.getDimensionBlacklist()) {
            ResourceLocation r = new ResourceLocation(s);
            if (Utils.dimensionToString(world).equals(r.toString())) {
                return false;
            }
        }

        if (func_207803_a(reader, rand, pos, pluton, plutonCapability)) {
            plutonCapability
                    .setOrePlutonGenerated(new ChunkPosDim(pos.getX(), pos.getZ(), Utils.dimensionToString(world)));
        }

        return false;
    }

    protected boolean func_207803_a(IWorld world, Random rand, BlockPos pos, IDeposit pluton,
            IGeolosysCapability plutonCapability) {

        if (pluton.getPlutonType() == PlutonType.DENSE) {
            if (FeatureUtils.generateDense(world, pos, rand, pluton, plutonCapability)) {
                this.postPlacement(world, pos, pluton);
                return true;
            }
            return false;
        }
        if (pluton.getPlutonType() == PlutonType.SPARSE) {
            if (FeatureUtils.generateSparse(world, pos, rand, pluton, plutonCapability)) {
                this.postPlacement(world, pos, pluton);
                return true;
            }
            return false;
        }
        if (pluton.getPlutonType() == PlutonType.DIKE) {
            if (FeatureUtils.generateDike(world, pos, rand, pluton, plutonCapability)) {
                this.postPlacement(world, pos, pluton);
                return true;
            }
            return false;
        }
        if (pluton.getPlutonType() == PlutonType.LAYER) {
            if (FeatureUtils.generateLayer(world, pos, rand, pluton, plutonCapability)) {
                this.postPlacement(world, pos, pluton);
                return true;
            }
            return false;
        }

        Geolosys.getInstance().LOGGER.error("Unknown Generation Logic for PlutonType {}", pluton.getPlutonType());
        return false;
    }

}