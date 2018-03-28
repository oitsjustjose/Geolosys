package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * A modified version of:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/common/world/IEWorldGen.java
 * Original Source & Credit: BluSunrize
 **/

public class OreGenerator implements IWorldGenerator
{
    private static ArrayList<OreGen> oreSpawnList = new ArrayList<>();
    private static int biggestWeight = 0;
    private OreGen lastGenerated = null;

    public static void addOreGen(IBlockState state, int maxVeinSize, int minY, int maxY, int weight, int[] blacklist)
    {
        if (weight > biggestWeight)
        {
            biggestWeight = weight;
        }
        OreGen gen = new OreGen(state, maxVeinSize, minY, maxY, weight, blacklist);
        oreSpawnList.add(gen);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        // Randomized chance for spawning to NOT happen:
        int minWeight = random.nextInt(biggestWeight);
        while (true)
        {
            OreGen check = oreSpawnList.get(random.nextInt(oreSpawnList.size()));
            if (check.weight > minWeight)
            {
                if (lastGenerated == null || lastGenerated.state != check.state)
                {
                    if (random.nextInt(100) < check.weight)
                    {
                        check.generate(world, random, (chunkX * 16), (chunkZ * 16));
                        this.lastGenerated = check;
                    }
                }
                return;
            }
        }
    }

    public static class OreGen
    {
        WorldGenMinable pluton;
        IBlockState state;
        int minY;
        int maxY;
        int weight;
        int[] blacklistedDims;

        public OreGen(IBlockState state, int maxVeinSize, int minY, int maxY, int weight, int[] blacklist)
        {
            this.pluton = new WorldGenMinable(state, maxVeinSize, iBlockState -> iBlockState != null && (GeolosysAPI.replacementMats.contains(iBlockState)));
            this.state = state;
            this.minY = Math.min(minY, maxY);
            this.maxY = Math.max(minY, maxY);
            this.weight = weight;
            this.blacklistedDims = blacklist;
        }

        public void generate(World world, Random rand, int x, int z)
        {
            if (!Geolosys.getInstance().chunkOreGen.canGenerateInChunk(new ChunkPos(x / 16, z / 16)))
            {
                return;
            }
            pluton.generate(world, rand, new BlockPos(x + rand.nextInt(4) + 4, minY != maxY ? minY + rand.nextInt(maxY - minY) : minY, z + rand.nextInt(4) + 4));
            GeolosysAPI.putWorldDeposit(new ChunkPos(x / 16, z / 16), state.toString().substring(0, state.toString().indexOf("[")) + ":" + state.getBlock().getMetaFromState(state));
            GeolosysAPI.writeToFile();
            Geolosys.getInstance().chunkOreGen.addChunk(new ChunkPos(x / 16, z / 16), world, GeolosysAPI.oreBlocks.get(state));
        }
    }
}
