package com.oitsjustjose.geolosys.common.world.feature;

import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.List;
import java.util.function.Supplier;

public class FeatureUtils {

    public static boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
        return new ChunkPos(pos).equals(chunkPos);
    }

//    public static ConfiguredFeature<?, ?> getFeature(ConfiguredFeature<?, ?> feature) {
//        TODO: Sort this out
//        ConfiguredFeature<?, ?> currentFeature = feature;
//        if (currentFeature.feature instanceof FeatureConfig) {
//            do {
//                currentFeature = ((DecoratedFeatureConfig) currentFeature.getConfig()).feature.get();
//            } while (currentFeature.feature instanceof DecoratedFeature);
//        }
//        OreFeature
//        return currentFeature;
//    }

    public static void destroyFeature(List<Supplier<ConfiguredFeature<?, ?>>> features,
            List<Supplier<ConfiguredFeature<?, ?>>> destroy) {
        for (Supplier<ConfiguredFeature<?, ?>> feature : destroy) {
            features.remove(feature);
        }
    }

    public static BlockState tryGetBlockState(LevelReader reader, ChunkPos chunk, BlockPos pos) {
        if (reader.hasChunk(chunk.x, chunk.z)) {
            return reader.getBlockState(pos);
        }

        return Blocks.BARRIER.defaultBlockState();
    }

    public static boolean tryPlaceBlock(WorldGenLevel reader, ChunkPos chunk, BlockPos pos, BlockState state,
                                        IDepositCapability cap) {
        if (isInChunk(chunk, pos) && reader.hasChunk(chunk.x, chunk.z)) {
            boolean ret = reader.setBlock(pos, state, 2 | 16);
            return ret;
        }

        cap.putPendingBlock(new BlockPos(pos), state);
        return false;
    }

    public static void fixSnowyBlock(WorldGenLevel reader, BlockPos posPlaced) {
        BlockState below = reader.getBlockState(posPlaced.below());
        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            reader.setBlock(posPlaced.below(), below.setValue(BlockStateProperties.SNOWY, Boolean.valueOf(false)),
                    2 | 16);
        }
    }
}
