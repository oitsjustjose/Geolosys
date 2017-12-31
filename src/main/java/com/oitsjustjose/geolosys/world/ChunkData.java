package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.blocks.BlockSample;
import com.oitsjustjose.geolosys.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.util.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;

import java.util.HashSet;
import java.util.Random;

public class ChunkData
{
    Random random = new Random();
    private HashSet<ChunkPos> populatedChunks = new HashSet<>();

    public void addChunk(ChunkPos pos, World world, IBlockState state)
    {
        populatedChunks.add(pos);
        if (world.getWorldType() == WorldType.FLAT)
        {
            return;
        }

        int cap = getSampleCount(state);
        Geolosys.getInstance().LOGGER.info(cap);
        for (int i = 0; i < cap; i++)
        {
            BlockPos p = getSamplePos(world, pos);

            if (world.getBlockState(p.down()).getBlock() instanceof BlockSample || world.getBlockState(p.down()).getBlock() instanceof BlockSampleVanilla)
            {
                continue;
            }
            if (Config.getInstance().generateSamplesInWater || !isMoist(world, p))
            {
                world.setBlockState(p, state);
            }
        }
    }

    public boolean canGenerateInChunk(ChunkPos pos)
    {
        return !populatedChunks.contains(pos);
    }

    private BlockPos getSamplePos(World world, ChunkPos chunkPos)
    {
        return world.getTopSolidOrLiquidBlock(new BlockPos((chunkPos.x << 4) + random.nextInt(16), 0, (chunkPos.z << 4) + random.nextInt(16)));
    }

    private boolean isMoist(World world, BlockPos pos)
    {
        return world.getBlockState(pos.up()).getMaterial().isLiquid() || world.getBlockState(pos.east()).getMaterial().isLiquid() || world.getBlockState(pos.west()).getMaterial().isLiquid() || world.getBlockState(pos.north()).getMaterial().isLiquid() || world.getBlockState(pos.south()).getMaterial().isLiquid();
    }

    private int getSampleCount(IBlockState state)
    {
        int count = 0;
        if (state.getBlock() == Geolosys.getInstance().ORE_SAMPLE)
        {
            switch (state.getBlock().getMetaFromState(state))
            {
                case 0:
                    count = Math.round(Geolosys.getInstance().configOres.hematiteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 1:
                    count = Math.round(Geolosys.getInstance().configOres.limoniteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 2:
                    count = Math.round(Geolosys.getInstance().configOres.malachiteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 3:
                    count = Math.round(Geolosys.getInstance().configOres.azuriteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 4:
                    count = Math.round(Geolosys.getInstance().configOres.cassiteriteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 5:
                    count = Math.round(Geolosys.getInstance().configOres.tealliteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 6:
                    count = Math.round(Geolosys.getInstance().configOres.galenaSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 7:
                    count = Math.round(Geolosys.getInstance().configOres.bauxiteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 8:
                    count = Math.round(Geolosys.getInstance().configOres.platinumSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 9:
                    count = Math.round(Geolosys.getInstance().configOres.autuniteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 10:
                    count = Math.round(Geolosys.getInstance().configOres.sphaleriteSize / (float)(Config.getInstance().maxSamples));
                    break;
            }
        }
        else if (state.getBlock() == Geolosys.getInstance().ORE_SAMPLE_VANILLA)
        {
            switch (state.getBlock().getMetaFromState(state))
            {
                case 0:
                    count = Math.round(Geolosys.getInstance().configOres.coalSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 1:
                    count = Math.round(Geolosys.getInstance().configOres.cinnabarSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 2:
                    count = Math.round(Geolosys.getInstance().configOres.goldSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 3:
                    count = Math.round(Geolosys.getInstance().configOres.lapisSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 4:
                    count = Math.round(Geolosys.getInstance().configOres.quartzSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 5:
                    count = Math.round(Geolosys.getInstance().configOres.kimberliteSize / (float)(Config.getInstance().maxSamples));
                    break;
                case 6:
                    count = Math.round(Geolosys.getInstance().configOres.berylSize / (float)(Config.getInstance().maxSamples));
                    break;
            }
        }

        // Normalize maximum sample counts
        if (count > Config.getInstance().maxSamples)
        {
            count = Config.getInstance().maxSamples;
        }

        return count;
    }
}