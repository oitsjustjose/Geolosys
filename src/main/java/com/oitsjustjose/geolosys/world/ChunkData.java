package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.blocks.BlockOreSample;
import com.oitsjustjose.geolosys.blocks.BlockOreSampleVanilla;
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
            return;

        int cap = random.nextInt(Geolosys.config.chanceSample - 1) + 1;
        for (int i = 0; i < cap; i++)
        {
            BlockPos p = getSamplePos(world, pos);
            if (world.getBlockState(p.down()).getBlock() instanceof BlockOreSample || world.getBlockState(p.down()).getBlock() instanceof BlockOreSampleVanilla);
                world.setBlockState(p, state);
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
}