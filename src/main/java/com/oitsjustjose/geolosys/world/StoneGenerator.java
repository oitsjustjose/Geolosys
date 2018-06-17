package com.oitsjustjose.geolosys.world;

import com.google.common.base.Predicate;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * A modified version of:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/common/world/IEWorldGen.java
 * Original Source & Credit: BluSunrize
 **/

public class StoneGenerator implements IWorldGenerator
{
    private static final Predicate<IBlockState> blockStatePredicate = iBlockState -> iBlockState != null && (GeolosysAPI.replacementMats.contains(iBlockState));
    private static ArrayList<StoneGen> stoneSpawnList = new ArrayList<>();
    private static final String dataID = "geolosysStoneGeneratorPending";

    public static void addStoneGen(IBlockState state, int minY, int maxY, int weight)
    {
        StoneGen gen = new StoneGen(state, minY, maxY, weight);
        stoneSpawnList.add(gen);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        ToDoBlocks.getForWorld(world, dataID).processPending(new ChunkPos(chunkX, chunkZ), world, blockStatePredicate);
        if (stoneSpawnList.size() > 0)
        {
            stoneSpawnList.get(random.nextInt(stoneSpawnList.size())).generate(world, random, (chunkX * 16), (chunkZ * 16));
        }
    }

    public static class StoneGen
    {
        WorldGenMinableSafe pluton;
        IBlockState state;
        int minY;
        int maxY;
        int weight;


        StoneGen(IBlockState state, int minY, int maxY, int weight)
        {
            this.pluton = new WorldGenMinableSafe(state, 96, blockStatePredicate, dataID);
            this.state = state;
            this.minY = Math.min(minY, maxY);
            this.maxY = Math.max(minY, maxY);
            this.weight = weight;
        }

        public void generate(World world, Random rand, int x, int z)
        {
            if (!Geolosys.getInstance().chunkOreGen.canGenerateInChunk(new ChunkPos(x / 16, z / 16), world.provider.getDimension()))
            {
                return;
            }
            if (rand.nextInt(100) < weight)
            {
                int y = minY != maxY ? minY + rand.nextInt(maxY - minY) : minY;
                pluton.generate(world, rand, new BlockPos(x + 8, y, z + 8));
            }
        }
    }
}
