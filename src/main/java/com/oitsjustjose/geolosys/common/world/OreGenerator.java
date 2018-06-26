package com.oitsjustjose.geolosys.common.world;

import com.google.common.base.Predicate;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Random;

/**
 * A modified version of:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/common/world/IEWorldGen.java
 * Original Source & Credit: BluSunrize
 **/

public class OreGenerator implements IWorldGenerator
{
    private static final Predicate<IBlockState> blockStatePredicate = iBlockState -> iBlockState != null && (GeolosysAPI.replacementMats.contains(iBlockState));
    private static final String dataID = "geolosysOreGeneratorPending";
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
        ToDoBlocks.getForWorld(world, dataID).processPending(new ChunkPos(chunkX, chunkZ), world, blockStatePredicate);
        if (oreSpawnList.size() > 0)
        {
            oreSpawnList.get(random.nextInt(oreSpawnList.size())).generate(world, random, (chunkX * 16), (chunkZ * 16));
        }
    }

    public static class OreGen
    {
        WorldGenMinableSafe pluton;
        IBlockState state;
        int minY;
        int maxY;
        int weight;
        int[] blacklistedDims;

        public OreGen(IBlockState state, int maxVeinSize, int minY, int maxY, int weight, int[] blacklist)
        {
            this.pluton = new WorldGenMinableSafe(state, maxVeinSize, blockStatePredicate, dataID);
            this.state = state;
            this.minY = Math.min(minY, maxY);
            this.maxY = Math.max(minY, maxY);
            this.weight = weight;
            this.blacklistedDims = blacklist;
        }

        public void generate(World world, Random rand, int x, int z)
        {
            if (!Geolosys.getInstance().chunkOreGen.canGenerateInChunk(new ChunkPos(x / 16, z / 16), world.provider.getDimension()))
            {
                return;
            }
            for (int d : this.blacklistedDims)
            {
                if (d == world.provider.getDimension())
                {
                    return;
                }
            }
            if (rand.nextInt(100) < weight)
            {
                int y = minY != maxY ? minY + rand.nextInt(maxY - minY) : minY;
                if (Loader.isModLoaded("twilightforest") && world.provider.getDimension() == 7)
                {
                    y /= 2;
                    y /= 2;
                }

                pluton.generate(world, rand, new BlockPos(x + 8, y, z + 8));
                GeolosysAPI.putWorldDeposit(new ChunkPos(x / 16, z / 16), world.provider.getDimension(), state.getBlock().getRegistryName() + ":" + state.getBlock().getMetaFromState(state));
                GeolosysAPI.writeToFile();
                Geolosys.getInstance().chunkOreGen.addChunk(new ChunkPos(x / 16, z / 16), world, GeolosysAPI.oreBlocks.get(state));
            }
        }
    }
}
