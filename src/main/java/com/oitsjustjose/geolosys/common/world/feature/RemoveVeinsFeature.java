package com.oitsjustjose.geolosys.common.world.feature;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RemoveVeinsFeature extends Feature<NoneFeatureConfiguration> {
    public RemoveVeinsFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        if (!CommonConfig.REMOVE_VANILLA_ORES.get()) {
            return true;
        }

        WorldGenLevel level = f.level();
        ChunkPos cp = new ChunkPos(f.origin());
        int rm = 0;

        for (int x = cp.getMinBlockX(); x <= cp.getMaxBlockX(); x++) {
            for (int z = cp.getMinBlockZ(); z <= cp.getMaxBlockZ(); z++) {
                for (int y = level.getMinBuildHeight(); y < 1; y++) {
                    BlockPos p = new BlockPos(x, y, z);
                    if (!FeatureUtils.isInChunk(cp, p)) {
                        continue;
                    }

                    BlockState state = level.getBlockState(p);
                    if (state.getBlock() == Blocks.RAW_IRON_BLOCK || state.getBlock() == Blocks.DEEPSLATE_IRON_ORE) {
                        level.setBlock(p, Blocks.TUFF.defaultBlockState(), 2 | 16);
                        rm++;
                    }
                }
            }
        }

        if (rm > 0 && CommonConfig.ADVANCED_DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.info("Removed {} vein blocks in chunk {}", rm, cp);
        }

        return true;
    }
}
