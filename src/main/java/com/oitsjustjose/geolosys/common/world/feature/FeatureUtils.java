package com.oitsjustjose.geolosys.common.world.feature;

import java.util.List;
import java.util.function.Supplier;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;

public class FeatureUtils {

    public static boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
        int blockX = pos.getX();
        int blockZ = pos.getZ();
        return blockX >= chunkPos.getXStart() && blockX <= chunkPos.getXEnd() && blockZ >= chunkPos.getZStart()
                && blockZ <= chunkPos.getZEnd();
    }

    public static ConfiguredFeature<?, ?> getFeature(ConfiguredFeature<?, ?> feature) {
        ConfiguredFeature<?, ?> currentFeature = feature;
        if (currentFeature.feature instanceof DecoratedFeature) {
            do {
                currentFeature = ((DecoratedFeatureConfig) currentFeature.getConfig()).feature.get();
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

    public static boolean tryPlaceBlock(ISeedReader reader, ChunkPos chunk, BlockPos pos, BlockState state,
            IDepositCapability cap) {
        if (isInChunk(chunk, pos) && reader.chunkExists(chunk.x, chunk.z)) {
            return reader.setBlockState(pos, state, 2 | 16);
        }

        cap.putPendingBlock(new BlockPos(pos), state);
        return false;
    }

    // public static boolean generateSparse(ISeedReader reader, BlockPos pos, Random
    // rand, IDeposit pluton,
    // IGeolosysCapability plutonCapability) {
    // ChunkPos thisChunk = new ChunkPos(pos);
    // boolean placed = false;

    // for (int y = pluton.getYMin(); y < pluton.getYMax(); y++) {
    // int numAttempts = rand.nextInt(pluton.getSize() / 3);
    // for (int attempt = 0; attempt < numAttempts; attempt++) {
    // int x = pos.getX() + rand.nextInt(16);
    // int z = pos.getZ() + rand.nextInt(16);
    // BlockPos blockpos = new BlockPos(x, y, z);
    // if (isInChunk(thisChunk, blockpos) && reader.chunkExists(thisChunk.x,
    // thisChunk.z)) {
    // float density = Math.min(pluton.getDensity(), 1.0F);

    // if (rand.nextFloat() > density) {
    // continue;
    // }
    // BlockState state = reader.getBlockState(blockpos);

    // for (BlockState matcherState : (pluton.getBlockStateMatchers() == null ?
    // Utils.getDefaultMatchers()
    // : pluton.getBlockStateMatchers())) {
    // if (Utils.doStatesMatch(matcherState, state)) {
    // reader.setBlockState(blockpos, pluton.getOre(), 2 | 16);
    // placed = true;
    // break;
    // }
    // }
    // } else {
    // plutonCapability.putPendingBlock(new BlockPos(pos,
    // Utils.ensionToString(reader)),
    // pluton.getOre());
    // }
    // }
    // }
    // return placed;
    // }

    // public static boolean generateDike(ISeedReader reader, BlockPos pos, Random
    // rand, IDeposit pluton,
    // IGeolosysCapability plutonCapability) {
    // ChunkPos thisChunk = new ChunkPos(pos);
    // boolean placed = false;

    // int height = Math.abs((pluton.getYMax() - pluton.getYMin()));

    // int x = thisChunk.getXStart() + rand.nextInt(16);
    // int y = pluton.getYMin() + rand.nextInt(height);
    // int z = thisChunk.getZStart() + rand.nextInt(16);

    // BlockPos basePos = new BlockPos(x, y, z);

    // int radius = (pluton.getSize() / height) > 0 ? (pluton.getSize() / height) :
    // (height / pluton.getSize());

    // for (int dY = y; dY <= pluton.getYMax(); dY++) {
    // for (int dX = -radius; dX <= radius; dX++) {
    // for (int dZ = -radius; dZ <= radius; dZ++) {
    // float dist = (dX * dX) + (dZ * dZ);
    // if (dist > radius) {
    // continue;
    // }

    // // basePos.add(dX, 0, dZ)
    // BlockPos blockpos = new BlockPos(basePos.getX() + dX, dY, basePos.getZ() +
    // dZ);

    // if (isInChunk(thisChunk, blockpos) && reader.chunkExists(thisChunk.x,
    // thisChunk.z)) {
    // float density = Math.min(pluton.getDensity(), 1.0F);

    // if (rand.nextFloat() > density) {
    // continue;
    // }
    // BlockState state = reader.getBlockState(blockpos);
    // for (BlockState matcherState : (pluton.getBlockStateMatchers() == null
    // ? Utils.getDefaultMatchers()
    // : pluton.getBlockStateMatchers())) {
    // if (Utils.doStatesMatch(matcherState, state)) {
    // reader.setBlockState(blockpos, pluton.getOre(), 2 | 16);
    // placed = true;
    // break;
    // }
    // }
    // } else {
    // plutonCapability.putPendingBlock(new BlockPos(pos,
    // Utils.ensionToString(reader)),
    // pluton.getOre());
    // placed = true;
    // }
    // }
    // }

    // // After a layer is done, *maybe* shrink it.
    // if (rand.nextInt(100) % pluton.getSize() == 0) {
    // radius -= 1;
    // if (radius < 0) {
    // return placed;
    // }
    // }
    // }
    // return placed;
    // }

    // public static boolean generateTopLayer(ISeedReader reader, BlockPos pos,
    // Random rand, IDeposit pluton,
    // IGeolosysCapability plutonCapability) {
    // ChunkPos thisChunk = new ChunkPos(pos);
    // boolean placed = false;

    // int x = ((thisChunk.getXStart() + thisChunk.getXEnd()) / 2) - rand.nextInt(8)
    // + rand.nextInt(16);
    // int z = ((thisChunk.getZStart() + thisChunk.getZEnd()) / 2) - rand.nextInt(8)
    // + rand.nextInt(16);
    // int radX = pluton.getSize() + rand.nextInt(Math.max(1, x %
    // pluton.getSize()));
    // int radZ = pluton.getSize() + rand.nextInt(Math.max(1, z %
    // pluton.getSize()));

    // BlockPos basePos = new BlockPos(x, 0, z);
    // HashSet<BlockState> matchers = pluton.getBlockStateMatchers() == null ?
    // Utils.getDefaultMatchers()
    // : pluton.getBlockStateMatchers();

    // for (int dX = -radZ; dX <= radZ; dX++) {
    // for (int dZ = -radZ; dZ <= radZ; dZ++) {

    // if (((dX * dX) + (dZ * dZ)) > pluton.getSize() + rand.nextInt(Math.max(1,
    // pluton.getSize() / 2))) {
    // continue;
    // }

    // BlockPos blockPos = Utils.getTopSolidBlock(reader, basePos.add(dX, 0, dZ));

    // for (int i = 0; i < ((radX + radZ) / 2) / 2; i++) {
    // blockPos = blockPos.add(0, -i, 0);
    // boolean isTopBlock = !reader.getBlockState(blockPos.up()).isSolid();

    // if (isInChunk(thisChunk, blockPos) && reader.chunkExists(thisChunk.x,
    // thisChunk.z)) {
    // float density = Math.min(pluton.getDensity(), 1.0F);
    // if (rand.nextFloat() > density) {
    // continue;
    // }

    // BlockState state = reader.getBlockState(blockPos);

    // for (BlockState matcherState : matchers) {
    // if (Utils.doStatesMatch(matcherState, state)) {

    // BlockState toPlace = pluton.getOre();
    // if (pluton.getOre().hasProperty(BlockStateProperties.BOTTOM)) {
    // toPlace = toPlace.with(BlockStateProperties.BOTTOM,
    // reader.getBlockState(blockPos.up()).isSolid());
    // }

    // reader.setBlockState(blockPos, toPlace, 2 | 16);
    // if (isTopBlock && reader.getBlockState(blockPos.up()).getMaterial() ==
    // Material.AIR
    // && rand.nextInt(5) == 0) {
    // reader.setBlockState(blockPos.up(), pluton.getSampleBlock(), 2 | 16);
    // }

    // placed = true;
    // break;
    // }
    // }

    // } else {
    // plutonCapability.putPendingBlock(new BlockPos(pos,
    // Utils.ensionToString(reader)),
    // pluton.getOre());
    // }
    // }
    // }
    // }

    // return placed;
    // }
}
