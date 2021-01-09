package com.oitsjustjose.geolosys.common.world.feature;

import java.util.List;
import java.util.Random;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.BlockPosDim;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.capability.IGeolosysCapability;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;

public class FeatureUtils {

    public static boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
        int blockX = pos.getX();
        int blockZ = pos.getZ();
        return blockX >= chunkPos.getXStart() && blockX <= chunkPos.getXEnd() && blockZ >= chunkPos.getZStart()
                && blockZ <= chunkPos.getZEnd();
    }

    public static boolean isInChunk(ChunkPos chunkPos, BlockPosDim pos) {
        int blockX = pos.getX();
        int blockZ = pos.getZ();
        return blockX >= chunkPos.getXStart() && blockX <= chunkPos.getXEnd() && blockZ >= chunkPos.getZStart()
                && blockZ <= chunkPos.getZEnd();
    }

    public static boolean generateDense(IWorld world, BlockPos pos, Random rand, IDeposit pluton,
            IGeolosysCapability plutonCapability) {

        // Do this ourselves because by default pos.getY() == 0
        int randY = pluton.getYMin() + rand.nextInt(pluton.getYMax() - pluton.getYMin());

        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = (float) (pos.getX() + 8) + MathHelper.sin(f) * (float) pluton.getSize() / 8.0F;
        double d1 = (float) (pos.getX() + 8) - MathHelper.sin(f) * (float) pluton.getSize() / 8.0F;
        double d2 = (float) (pos.getZ() + 8) + MathHelper.cos(f) * (float) pluton.getSize() / 8.0F;
        double d3 = (float) (pos.getZ() + 8) - MathHelper.cos(f) * (float) pluton.getSize() / 8.0F;
        double d4 = randY + rand.nextInt(3) - 2;
        double d5 = randY + rand.nextInt(3) - 2;

        boolean placed = false;

        for (int i = 0; i < pluton.getSize(); ++i) {
            float f1 = (float) i / (float) pluton.getSize();
            double d6 = d0 + (d1 - d0) * (double) f1;
            double d7 = d4 + (d5 - d4) * (double) f1;
            double d8 = d2 + (d3 - d2) * (double) f1;
            double d9 = rand.nextDouble() * (double) pluton.getSize() / 16.0D;
            double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1) {
                double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D) {
                            for (int j2 = l; j2 <= k1; ++j2) {
                                double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                                    BlockPos blockpos = new BlockPos(l1, i2, j2);

                                    if (world.chunkExists(l1 >> 4, j2 >> 4)) {
                                        float density = Math.min(pluton.getDensity(), 1.0F);

                                        if (rand.nextFloat() > density) {
                                            continue;
                                        }
                                        BlockState state = world.getBlockState(blockpos);
                                        for (BlockState matcherState : (pluton.getBlockStateMatchers() == null
                                                ? Utils.getDefaultMatchers()
                                                : pluton.getBlockStateMatchers())) {
                                            if (Utils.doStatesMatch(matcherState, state)) {
                                                world.setBlockState(blockpos, pluton.getOre(), 2 | 16);
                                                placed = true;
                                                break;
                                            }
                                        }
                                    } else {
                                        plutonCapability.putPendingBlock(
                                                new BlockPosDim(pos, Utils.dimensionToString(world)), pluton.getOre());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return placed;
    }

    public static boolean generateSparse(IWorld world, BlockPos pos, Random rand, IDeposit pluton,
            IGeolosysCapability plutonCapability) {
        ChunkPos thisChunk = new ChunkPos(pos);
        boolean placed = false;

        for (int y = pluton.getYMin(); y < pluton.getYMax(); y++) {
            int numAttempts = rand.nextInt(pluton.getSize() / 3);
            for (int attempt = 0; attempt < numAttempts; attempt++) {
                int x = pos.getX() + rand.nextInt(16);
                int z = pos.getZ() + rand.nextInt(16);
                BlockPos blockpos = new BlockPos(x, y, z);
                if (isInChunk(thisChunk, blockpos) || world.chunkExists(x >> 4, z >> 4)) {
                    float density = Math.min(pluton.getDensity(), 1.0F);

                    if (rand.nextFloat() > density) {
                        continue;
                    }
                    BlockState state = world.getBlockState(blockpos);

                    for (BlockState matcherState : (pluton.getBlockStateMatchers() == null ? Utils.getDefaultMatchers()
                            : pluton.getBlockStateMatchers())) {
                        if (Utils.doStatesMatch(matcherState, state)) {
                            world.setBlockState(blockpos, pluton.getOre(), 2 | 16);
                            placed = true;
                            break;
                        }
                    }
                } else {
                    plutonCapability.putPendingBlock(new BlockPosDim(pos, Utils.dimensionToString(world)),
                            pluton.getOre());
                }
            }
        }
        return placed;
    }

    public static boolean generateDike(IWorld world, BlockPos pos, Random rand, IDeposit pluton,
            IGeolosysCapability plutonCapability) {
        ChunkPos thisChunk = new ChunkPos(pos);
        boolean placed = false;

        int height = Math.abs((pluton.getYMax() - pluton.getYMin()));

        int x = thisChunk.getXStart() + rand.nextInt(16);
        int y = pluton.getYMin() + rand.nextInt(height);
        int z = thisChunk.getZStart() + rand.nextInt(16);

        BlockPos basePos = new BlockPos(x, y, z);

        int radius = (pluton.getSize() / height) > 0 ? (pluton.getSize() / height) : (height / pluton.getSize());

        for (int dY = y; dY <= pluton.getYMax(); dY++) {
            for (int dX = -radius; dX <= radius; dX++) {
                for (int dZ = -radius; dZ <= radius; dZ++) {
                    float dist = (dX * dX) + (dZ * dZ);
                    if (dist > radius) {
                        continue;
                    }

                    // basePos.add(dX, 0, dZ)
                    BlockPos blockpos = new BlockPos(basePos.getX() + dX, dY, basePos.getZ() + dZ);

                    if (isInChunk(thisChunk, blockpos) || world.chunkExists(x >> 4, z >> 4)) {
                        float density = Math.min(pluton.getDensity(), 1.0F);

                        if (rand.nextFloat() > density) {
                            continue;
                        }
                        BlockState state = world.getBlockState(blockpos);
                        for (BlockState matcherState : (pluton.getBlockStateMatchers() == null
                                ? Utils.getDefaultMatchers()
                                : pluton.getBlockStateMatchers())) {
                            if (Utils.doStatesMatch(matcherState, state)) {
                                world.setBlockState(blockpos, pluton.getOre(), 2 | 16);
                                placed = true;
                                break;
                            }
                        }
                    } else {
                        plutonCapability.putPendingBlock(new BlockPosDim(pos, Utils.dimensionToString(world)),
                                pluton.getOre());
                        placed = true;
                    }
                }
            }

            // After a layer is done, *maybe* shrink it.
            if (rand.nextInt(100) % pluton.getSize() == 0) {
                radius -= 1;
                if (radius < 0) {
                    return placed;
                }
            }
        }
        return placed;
    }

    public static boolean generateLayer(IWorld world, BlockPos pos, Random rand, IDeposit pluton,
            IGeolosysCapability plutonCapability) {
        ChunkPos thisChunk = new ChunkPos(pos);
        boolean placed = false;

        int x = ((thisChunk.getXStart() + thisChunk.getXEnd()) / 2) - rand.nextInt(8) + rand.nextInt(16);
        int y = pluton.getYMin() + rand.nextInt(Math.abs(pluton.getYMax() - pluton.getYMin()));
        int z = ((thisChunk.getZStart() + thisChunk.getZEnd()) / 2) - rand.nextInt(8) + rand.nextInt(16);

        int dXMod = rand.nextInt(pluton.getSize());
        int dZMod = rand.nextInt(pluton.getSize());

        BlockPos basePos = new BlockPos(x, y, z);

        int radius = pluton.getSize() / 2;
        int depth = Math.max(1 + rand.nextInt(1), pluton.getSize() / 5);

        for (int dX = -radius; dX <= radius; dX++) {
            for (int dZ = -radius; dZ <= radius; dZ++) {
                for (int dY = 0; dY < depth; dY++) {
                    float dist = ((dX + dXMod) * (dX + dXMod)) + ((dZ + dZMod) * (dZ + dZMod));
                    if (dist > radius) {
                        continue;
                    }

                    BlockPos blockpos = basePos.add(dX, dY, dZ);

                    if (isInChunk(thisChunk, blockpos) || world.chunkExists(x >> 4, z >> 4)) {
                        float density = Math.min(pluton.getDensity(), 1.0F);

                        if (rand.nextFloat() > density) {
                            continue;
                        }
                        BlockState state = world.getBlockState(blockpos);
                        for (BlockState matcherState : (pluton.getBlockStateMatchers() == null
                                ? Utils.getDefaultMatchers()
                                : pluton.getBlockStateMatchers())) {
                            if (Utils.doStatesMatch(matcherState, state)) {
                                world.setBlockState(blockpos, pluton.getOre(), 2 | 16);
                                placed = true;
                                break;
                            }
                        }
                    } else {
                        plutonCapability.putPendingBlock(new BlockPosDim(pos, Utils.dimensionToString(world)),
                                pluton.getOre());
                    }
                }
            }
        }
        return placed;
    }

    public static boolean generateTopLayer(IWorld world, BlockPos pos, Random rand, IDeposit pluton,
            IGeolosysCapability plutonCapability) {
        ChunkPos thisChunk = new ChunkPos(pos);
        boolean placed = false;

        int x = ((thisChunk.getXStart() + thisChunk.getXEnd()) / 2) - rand.nextInt(8) + rand.nextInt(16);
        int z = ((thisChunk.getZStart() + thisChunk.getZEnd()) / 2) - rand.nextInt(8) + rand.nextInt(16);
        int radX = pluton.getSize() + rand.nextInt(Math.max(1, x % pluton.getSize()));
        int radZ = pluton.getSize() + rand.nextInt(Math.max(1, z % pluton.getSize()));

        BlockPos basePos = new BlockPos(x, 0, z);
        List<BlockState> matchers = pluton.getBlockStateMatchers() == null ? Utils.getDefaultMatchers()
                : pluton.getBlockStateMatchers();

        for (int dX = -radZ; dX <= radZ; dX++) {
            for (int dZ = -radZ; dZ <= radZ; dZ++) {

                if (((dX * dX) + (dZ * dZ)) > pluton.getSize() + rand.nextInt(Math.max(1, pluton.getSize() / 2))) {
                    continue;
                }

                BlockPos blockPos = Utils.getTopSolidBlock(world, basePos.add(dX, 0, dZ));

                for (int i = 0; i < ((radX + radZ) / 2) / 2; i++) {
                    blockPos = blockPos.add(0, -i, 0);
                    boolean isTopBlock = !world.getBlockState(blockPos.up()).isSolid();

                    if (isInChunk(thisChunk, blockPos) || world.chunkExists(x >> 4, z >> 4)) {
                        float density = Math.min(pluton.getDensity(), 1.0F);
                        if (rand.nextFloat() > density) {
                            continue;
                        }

                        BlockState state = world.getBlockState(blockPos);

                        for (BlockState matcherState : matchers) {
                            if (Utils.doStatesMatch(matcherState, state)) {

                                BlockState toPlace = pluton.getOre();
                                if (pluton.getOre().hasProperty(BlockStateProperties.BOTTOM)) {
                                    toPlace = toPlace.with(BlockStateProperties.BOTTOM,
                                            world.getBlockState(blockPos.up()).isSolid());
                                }

                                world.setBlockState(blockPos, toPlace, 2 | 16);
                                if (isTopBlock && world.getBlockState(blockPos.up()).getMaterial() == Material.AIR
                                        && rand.nextInt(5) == 0) {
                                    world.setBlockState(blockPos.up(), pluton.getSampleBlock(), 2 | 16);
                                }

                                placed = true;
                                break;
                            }
                        }

                    } else {
                        plutonCapability.putPendingBlock(new BlockPosDim(pos, Utils.dimensionToString(world)),
                                pluton.getOre());
                    }
                }

            }
        }

        if (placed && CommonConfig.DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.debug("Generated {} in Chunk {} (Pos [{} {} {}])", pluton.getFriendlyName(),
                    new ChunkPos(pos), pos.getX(), pos.getY(), pos.getZ());
        }

        return placed;
    }
}
