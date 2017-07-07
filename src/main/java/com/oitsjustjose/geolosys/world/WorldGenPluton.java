package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * A modified version of:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/common/world/IEWorldGen.java
 * Original Source & Credit: BluSunrize
 *
 **/

public class WorldGenPluton implements IWorldGenerator
{
    public static class OreGen
    {
        WorldGenMinable mineableGen;
        int minY;
        int maxY;
        int chunkOccurence;
        int weight;

        public OreGen(IBlockState state, int maxVeinSize, Block replaceTarget, int minY, int maxY, int chunkOccurence, int weight)
        {
            this.mineableGen = new WorldGenMinable(state, maxVeinSize, BlockMatcher.forBlock(replaceTarget));
            this.minY = minY;
            this.maxY = maxY;
            this.chunkOccurence = chunkOccurence;
            this.weight = weight;
        }

        public void generate(World world, Random rand, int x, int z)
        {
            BlockPos pos;
            for (int i = 0; i < chunkOccurence; i++)
                if (rand.nextInt(100) < weight)
                {
                    pos = new BlockPos(x + 8, minY + rand.nextInt(maxY - minY), z + 8);
                    mineableGen.generate(world, rand, pos);
                }
        }
    }

    public static ArrayList<OreGen> orespawnList = new ArrayList();

    public static OreGen addOreGen(IBlockState state, int maxVeinSize, int minY, int maxY, int chunkOccurence, int weight)
    {
        OreGen gen = new OreGen(state, maxVeinSize, Blocks.STONE, minY, maxY, chunkOccurence, weight);
        orespawnList.add(gen);
        return gen;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        this.generateOres(random, chunkX, chunkZ, world);
    }

    public void generateOres(Random random, int chunkX, int chunkZ, World world)
    {
        if (!isDIMBlacklisted(world.provider.getDimension()))
            for (OreGen gen : orespawnList)
                gen.generate(world, random, (chunkX * 16), (chunkZ * 16));
    }

    public boolean isDIMBlacklisted(int dim)
    {
        for (int d : Geolosys.config.blacklistedDIMs)
            if (d == dim)
                return true;
        return false;
    }

}
