package com.oitsjustjose.geolosys.common.world.feature;

import java.util.List;
import java.util.function.Supplier;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;

public class FeatureUtils {

    public static boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
        return new ChunkPos(pos).equals(chunkPos);
    }

    public static ConfiguredFeature<?, ?> getFeature(ConfiguredFeature<?, ?> feature) {
        ConfiguredFeature<?, ?> currentFeature = feature;
        if (currentFeature.feature instanceof DecoratedFeature) {
            do {
                currentFeature =
                        ((DecoratedFeatureConfig) currentFeature.getConfig()).feature.get();
            } while (currentFeature.feature instanceof DecoratedFeature);
        }
        return currentFeature;
    }

    public static void destroyFeature(List<Supplier<ConfiguredFeature<?, ?>>> features,
            List<Supplier<ConfiguredFeature<?, ?>>> destroy) {
        for (Supplier<ConfiguredFeature<?, ?>> feature : destroy) {
            features.remove(feature);
        }
    }

    public static BlockState tryGetBlockState(ISeedReader reader, ChunkPos chunk, BlockPos pos) {
        if (reader.chunkExists(chunk.x, chunk.z)) {
            return reader.getBlockState(pos);
        }

        return Blocks.BARRIER.getDefaultState();
    }

    public static boolean tryPlaceBlock(ISeedReader reader, ChunkPos chunk, BlockPos pos,
            BlockState state, IDepositCapability cap) {
        if (isInChunk(chunk, pos) && reader.chunkExists(chunk.x, chunk.z)) {
            boolean ret = reader.setBlockState(pos, state, 2 | 16);
            if (!ret) {
                Geolosys.getInstance().LOGGER.info("FUK");
            }
            return ret;
        }

        cap.putPendingBlock(new BlockPos(pos), state);
        return false;
    }

    public static void fixSnowyBlock(ISeedReader reader, BlockPos posPlaced) {
        BlockState below = reader.getBlockState(posPlaced.down());
        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            reader.setBlockState(posPlaced.down(),
                    below.with(BlockStateProperties.SNOWY, Boolean.valueOf(false)), 2 | 16);
        }
    }
}
