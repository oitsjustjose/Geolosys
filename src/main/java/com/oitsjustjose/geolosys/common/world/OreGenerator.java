package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.api.GeolosysSaveData;
import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Loader;

/**
 * A modified version of:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/common/world/IEWorldGen.java
 * Original Source & Credit: BluSunrize
 **/

public class OreGenerator implements IWorldGenerator
{
    private static final List<IBlockState> blockStateMatchers = GeolosysAPI.replacementMats;
    private static final String dataID = "geolosysOreGeneratorPending";
    private static ArrayList<OreGen> oreSpawnList = new ArrayList<>();
    private static int biggestWeight = 0;

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
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
            IChunkProvider chunkProvider)
    {
        ToDoBlocks.getForWorld(world, dataID).processPending(new ChunkPos(chunkX, chunkZ), world, blockStateMatchers);
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
            this.pluton = new WorldGenMinableSafe(state, maxVeinSize,
                    doesOreHaveSpecialMatchers(state) ? GeolosysAPI.oreBlocksSpecific.get(state) : blockStateMatchers,
                    dataID);
            this.state = state;
            this.minY = Math.min(minY, maxY);
            this.maxY = Math.max(minY, maxY);
            this.weight = weight;
            this.blacklistedDims = blacklist;
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

                pluton.generate(world, rand, new BlockPos(x, y, z));
                GeolosysAPI.putWorldDeposit(new ChunkPos(x / 16, z / 16), world.provider.getDimension(),
                        state.getBlock().getRegistryName() + ":" + state.getBlock().getMetaFromState(state));
                GeolosysSaveData.get(world).markDirty();
                Geolosys.getInstance().chunkOreGen.addChunk(new ChunkPos(x / 16, z / 16), world,
                        GeolosysAPI.oreBlocks.get(state), y);
            }
            ForgeModContainer.logCascadingWorldGeneration = lastState;
        }

        /**
         * Finds out whether or not there's a special predicate for an ore block
         *
         * @param state The state to check with
         * @return True if the keyset contains the state, false otherwise
         */
        private boolean doesOreHaveSpecialMatchers(IBlockState state)
        {
            for (IBlockState iBlockState : GeolosysAPI.oreBlocksSpecific.keySet())
            {
                if (Utils.doStatesMatch(iBlockState, state))
                {
                    return true;
                }
            }
            return false;
        }
    }
}
