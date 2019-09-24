package com.oitsjustjose.geolosys.common.world.feature;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;

public class PlutonFeature extends Feature<PlutonFeatureConfig>
{
    public PlutonFeature(Function<Dynamic<?>, ? extends PlutonFeatureConfig> configFactoryIn)
    {
        super(configFactoryIn, true);
    }

    private boolean isInChunk(ChunkPos chunkPos, BlockPos pos)
    {
        int blockX = pos.getX();
        int blockZ = pos.getZ();
        return blockX >= chunkPos.getXStart() && blockX <= chunkPos.getXEnd() && blockZ >= chunkPos.getZStart()
                && blockZ <= chunkPos.getZEnd();
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
            BlockPos pos, PlutonFeatureConfig config)
    {
        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = (double) ((float) (pos.getX() + 8)
                + MathHelper.sin(f) * (float) config.getPluton().getSize() / 8.0F);
        double d1 = (double) ((float) (pos.getX() + 8)
                - MathHelper.sin(f) * (float) config.getPluton().getSize() / 8.0F);
        double d2 = (double) ((float) (pos.getZ() + 8)
                + MathHelper.cos(f) * (float) config.getPluton().getSize() / 8.0F);
        double d3 = (double) ((float) (pos.getZ() + 8)
                - MathHelper.cos(f) * (float) config.getPluton().getSize() / 8.0F);
        double d4 = (double) (pos.getY() + rand.nextInt(3) - 2);
        double d5 = (double) (pos.getY() + rand.nextInt(3) - 2);

        // ToDoBlocks toDoBlocks = ToDoBlocks.getForWorld(worldIn, dataName);
        ChunkPos thisChunk = new ChunkPos(pos);
        boolean placedOre = false;

        for (int i = 0; i < config.getPluton().getSize(); ++i)
        {
            float f1 = (float) i / (float) config.getPluton().getSize();
            double d6 = d0 + (d1 - d0) * (double) f1;
            double d7 = d4 + (d5 - d4) * (double) f1;
            double d8 = d2 + (d3 - d2) * (double) f1;
            double d9 = rand.nextDouble() * (double) config.getPluton().getSize() / 16.0D;
            double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1)
            {
                double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D)
                {
                    for (int i2 = k; i2 <= j1; ++i2)
                    {
                        double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D)
                        {
                            for (int j2 = l; j2 <= k1; ++j2)
                            {
                                double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D)
                                {
                                    BlockPos blockpos = new BlockPos(l1, i2, j2);

                                    if (isInChunk(thisChunk, blockpos) || worldIn.chunkExists(l1 >> 4, j2 >> 4))
                                    {
                                        float density = config.getPluton().getDensity() > 1.0F ? 1.0F
                                                : config.getPluton().getDensity();

                                        if (rand.nextFloat() > density)
                                        {
                                            continue;
                                        }
                                        BlockState state = worldIn.getBlockState(blockpos);
                                        if (state != null)
                                        {
                                            // If it has custom blockstate matcher:
                                            if (config.getPluton().getBlockStateMatchers() != null)
                                            {
                                                for (BlockState BlockState : config.getPluton().getBlockStateMatchers())
                                                {
                                                    if (Utils.doStatesMatch(BlockState, state))
                                                    {
                                                        worldIn.setBlockState(blockpos, config.getPluton().getOre(),
                                                                2 | 16);
                                                        placedOre = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            // Otherwise just use the default
                                            else
                                            {
                                                for (BlockState BlockState : GeolosysAPI.replacementMats)
                                                {
                                                    if (Utils.doStatesMatch(BlockState, state))
                                                    {
                                                        worldIn.setBlockState(blockpos, config.getPluton().getOre(),
                                                                2 | 16);
                                                        placedOre = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        // toDoBlocks.storePending(blockpos, config.getPluton().getOre());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return placedOre;
    }
}