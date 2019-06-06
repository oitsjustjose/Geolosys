package com.oitsjustjose.geolosys.common.world;

import java.util.HashMap;
import java.util.Random;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.world.DepositStone;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * A modified version of:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/common/world/IEWorldGen.java
 * Original Source & Credit: BluSunrize
 **/

public class StoneGenerator implements IWorldGenerator
{
    private static final String dataID = "geolosysStoneGeneratorPending";
    private static HashMap<Integer, StoneGen> stoneSpawnWeights = new HashMap<>();
    private static int last = 0;

    public static void addStoneGen(DepositStone stone)
    {
        StoneGen gen = new StoneGen(stone);
        for (int i = last; i < last + stone.getChance(); i++)
        {
            stoneSpawnWeights.put(i, gen);
        }
        last = last + stone.getChance();
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
            IChunkProvider chunkProvider)
    {
        ToDoBlocks.getForWorld(world, dataID).processPending(new ChunkPos(chunkX, chunkZ), world);
        if (world.provider.getDimension() == 1 || world.provider.getDimension() == -1)
        {
            return;
        }
        if (stoneSpawnWeights.keySet().size() > 0)
        {
            int rng = random.nextInt(stoneSpawnWeights.keySet().size());
            stoneSpawnWeights.get(rng).generate(world, random, (chunkX * 16), (chunkZ * 16));
        }
    }

    public static class StoneGen
    {
        WorldGenMinableSafe pluton;
        DepositStone depositStone;

        StoneGen(DepositStone depositStone)
        {
            this.pluton = new WorldGenMinableSafe(depositStone, dataID);
            this.depositStone = depositStone;
        }

        public void generate(World world, Random rand, int x, int z)
        {
            if (!Geolosys.getInstance().chunkOreGen.canGenerateInChunk(world, new ChunkPos(x / 16, z / 16),
                    world.provider.getDimension()))
            {
                return;
            }
            boolean lastState = ForgeModContainer.logCascadingWorldGeneration;
            ForgeModContainer.logCascadingWorldGeneration = false;
            for (int d : this.depositStone.getDimensionBlacklist())
            {
                if (d == world.provider.getDimension())
                {
                    return;
                }
            }

            if (rand.nextInt(100) < this.depositStone.getChance())
            {
                int y = this.depositStone.getYMin() != this.depositStone.getYMax()
                        ? this.depositStone.getYMin()
                                + rand.nextInt(this.depositStone.getYMax() - this.depositStone.getYMin())
                        : this.depositStone.getYMin();
                pluton.generate(world, rand, new BlockPos(x, y, z));
            }
            ForgeModContainer.logCascadingWorldGeneration = lastState;
        }
    }
}
