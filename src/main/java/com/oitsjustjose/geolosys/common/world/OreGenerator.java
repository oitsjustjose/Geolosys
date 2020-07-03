package com.oitsjustjose.geolosys.common.world;

import java.util.HashMap;
import java.util.Random;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.common.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.common.api.world.IOre;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.util.GeolosysSaveData;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
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

public class OreGenerator implements IWorldGenerator {
    private static final String dataID = "geolosysOreGeneratorPending";
    private static HashMap<Integer, OreGen> oreSpawnWeights = new HashMap<>();
    private static int last = 0;

    public static void addOreGen(IOre ore) {
        OreGen gen = new OreGen(ore);
        for (int i = last; i < last + ore.getChance(); i++) {
            oreSpawnWeights.put(i, gen);
        }
        last = last + ore.getChance();
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
            IChunkProvider chunkProvider) {
        ToDoBlocks.getForWorld(world, dataID).processPending(new ChunkPos(chunkX, chunkZ), world);

        if (oreSpawnWeights.keySet().size() > 0) {
            int rng = random.nextInt(oreSpawnWeights.keySet().size());

            // 50% chance to prefer an ore exclusive to that biome
            if (random.nextBoolean()) {
                Biome biome = world.getBiome(new BlockPos((chunkX * 16), world.getSeaLevel(), (chunkZ * 16)));
                for (int i = 0; i < oreSpawnWeights.keySet().size() / 2; i++) {
                    int newRNG = random.nextInt(oreSpawnWeights.keySet().size());
                    if (oreSpawnWeights.get(newRNG).ore instanceof DepositBiomeRestricted) {
                        if (((DepositBiomeRestricted) oreSpawnWeights.get(newRNG).ore).canPlaceInBiome(biome)) {
                            rng = newRNG;
                            break;
                        }
                    } else if (oreSpawnWeights.get(newRNG).ore instanceof DepositMultiOreBiomeRestricted) {
                        if (((DepositMultiOreBiomeRestricted) oreSpawnWeights.get(newRNG).ore).canPlaceInBiome(biome)) {
                            rng = newRNG;
                            break;
                        }
                    }
                }
            }

            // Check the biome
            if (oreSpawnWeights.get(rng).ore instanceof DepositBiomeRestricted) {
                DepositBiomeRestricted deposit = (DepositBiomeRestricted) oreSpawnWeights.get(rng).ore;
                if (deposit.canPlaceInBiome(world.getBiome(new BlockPos((chunkX * 16), 256, (chunkZ * 16))))) {
                    oreSpawnWeights.get(rng).generate(world, random, (chunkX * 16), (chunkZ * 16));
                } else {
                }
            } else if (oreSpawnWeights.get(rng).ore instanceof DepositMultiOreBiomeRestricted) {
                DepositMultiOreBiomeRestricted deposit = (DepositMultiOreBiomeRestricted) oreSpawnWeights.get(rng).ore;
                if (deposit.canPlaceInBiome(world.getBiome(new BlockPos((chunkX * 16), 256, (chunkZ * 16))))) {
                    oreSpawnWeights.get(rng).generate(world, random, (chunkX * 16), (chunkZ * 16));
                } else {
                }
            }
            // Not special
            else {
                oreSpawnWeights.get(rng).generate(world, random, (chunkX * 16), (chunkZ * 16));
            }
        }
    }

    public static class OreGen {
        WorldGenMinableSafe pluton;
        IOre ore;

        public OreGen(IOre ore) {
            this.pluton = new WorldGenMinableSafe(ore, dataID);
            this.ore = ore;
        }

        public void generate(World world, Random rand, int x, int z) {
            if (!Geolosys.getInstance().chunkOreGen.canGenerateInChunk(world, new ChunkPos(x / 16, z / 16),
                    world.provider.getDimension())) {
                return;
            }
            boolean lastState = ForgeModContainer.logCascadingWorldGeneration;
            ForgeModContainer.logCascadingWorldGeneration = false;
            for (int d : this.ore.getDimensionBlacklist()) {
                if (d == world.provider.getDimension()) {
                    return;
                }
            }
            if (rand.nextInt(100) < this.ore.getChance()) {
                int y = this.ore.getYMin() != this.ore.getYMax()
                        ? this.ore.getYMin() + rand.nextInt(this.ore.getYMax() - this.ore.getYMin())
                        : this.ore.getYMin();
                if (Loader.isModLoaded("twilightforest") && world.provider.getDimension() == 7) {
                    y /= 2;
                    y /= 2;
                }
                // If the pluton placed any ores at all
                if (pluton.generate(world, rand, new BlockPos(x, y, z))) {
                    IBlockState tmp = this.ore.getOre();
                    GeolosysAPI.putWorldDeposit(new ChunkPos(x / 16, z / 16), world.provider.getDimension(),
                            tmp.getBlock().getRegistryName() + ":" + tmp.getBlock().getMetaFromState(tmp));
                    GeolosysSaveData.get(world).markDirty();
                    Geolosys.getInstance().chunkOreGen.addChunk(new ChunkPos(x / 16, z / 16), world, y, this.ore);
                }
            }
            ForgeModContainer.logCascadingWorldGeneration = lastState;
        }
    }
}
