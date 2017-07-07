package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.util.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenPluton implements IWorldGenerator
{
    private Config config = Geolosys.config;
    IBlockState ore;
    int rarity;
    WorldGenMinable overworld;

    public WorldGenPluton(IBlockState state, int rarity)
    {
        this.ore = state;
        this.rarity = rarity;
        this.overworld = new WorldGenMinable(state, 24);
    }

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (isDIMBlacklisted(world.provider.getDimension()))
        {
            return;
        }
        for (int i = 0; i < rarity; i++)
        {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = rand.nextInt(70);
            int z = chunkZ * 16 + rand.nextInt(16);

            overworld.generate(world, rand, new BlockPos(x, y, z));
        }
    }


    private boolean isDIMBlacklisted(int dim)
    {
        for (int d : config.blacklistedDIMs)
        {
            if (d == dim)
            {
                return true;
            }
        }
        return false;
    }
}