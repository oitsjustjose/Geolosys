package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.HashMap;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.blocks.Types;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.oredict.OreDictionary;

public class Deposits
{
    private static HashMap<Integer, IBlockState> oreVanillaMap;
    private static HashMap<Integer, IBlockState> sampleVanillaMap;
    private static HashMap<Integer, IBlockState> oreMap;
    private static HashMap<Integer, IBlockState> sampleMap;

    public static void init()
    {
        oreVanillaMap = new HashMap<>();
        for (int i = 0; i < Types.Vanilla.values().length; i++)
        {
            oreVanillaMap.put(i, Utils.getStateFromMeta(Geolosys.getInstance().ORE_VANILLA, i));
        }
        sampleVanillaMap = new HashMap<>();
        for (int i = 0; i < Types.Vanilla.values().length; i++)
        {
            sampleVanillaMap.put(i, Utils.getStateFromMeta(Geolosys.getInstance().ORE_SAMPLE_VANILLA, i));
        }
        oreMap = new HashMap<>();
        for (int i = 0; i < Types.Modded.values().length; i++)
        {
            oreMap.put(i, Utils.getStateFromMeta(Geolosys.getInstance().ORE, i));
        }
        sampleMap = new HashMap<>();
        for (int i = 0; i < Types.Modded.values().length; i++)
        {
            sampleMap.put(i, Utils.getStateFromMeta(Geolosys.getInstance().ORE_SAMPLE, i));
        }
    }

    public static void register()
    {
        init();

        ConfigOres configOres = Geolosys.getInstance().configOres;
        // Vanilla World Generation:
        if (configOres.coal.getChance() > 0)
        {
            // Standard world generation
            GeolosysAPI.registerMineralDeposit(oreVanillaMap.get(0), sampleVanillaMap.get(0), configOres.coal);
        }
        if (configOres.cinnabar.getChance() > 0)
        {
            // Standard world generation
            GeolosysAPI.registerMineralDeposit(oreVanillaMap.get(1), sampleVanillaMap.get(1), configOres.cinnabar);
        }
        if (configOres.gold.getChance() > 0)
        {
            // Standard world generation
            GeolosysAPI.registerMineralDeposit(oreVanillaMap.get(2), sampleVanillaMap.get(2), configOres.gold);
        }
        if (configOres.lapis.getChance() > 0)
        {
            // Only generate in Desert-like biomes
            ArrayList<Biome> biomes = new ArrayList<>();
            biomes.add(Biomes.DESERT);
            biomes.add(Biomes.DESERT_HILLS);
            biomes.add(Biomes.MUTATED_DESERT);
            // If limestone is available, only generate in limestone
            if (OreDictionary.doesOreNameExist("stoneLimestone") && OreDictionary.getOres("stoneLimestone").size() > 0)
            {
                ArrayList<IBlockState> matchers = new ArrayList<>();
                for (ItemStack i : OreDictionary.getOres("stoneLimestone"))
                {
                    matchers.add(Utils.getStateFromMeta(Block.getBlockFromItem(i.getItem()), i.getMetadata()));
                }
                GeolosysAPI.registerMineralDeposit(oreVanillaMap.get(3), sampleVanillaMap.get(3),
                        configOres.lapis.getMinY(), configOres.lapis.getMaxY(), configOres.lapis.getSize(),
                        configOres.lapis.getChance(), configOres.lapis.getBlacklist(), matchers, biomes, true);
            }
            // Otherwise generate in anything
            else
            {
                GeolosysAPI.registerMineralDeposit(oreVanillaMap.get(3), sampleVanillaMap.get(3),
                        configOres.lapis.getMinY(), configOres.lapis.getMaxY(), configOres.lapis.getSize(),
                        configOres.lapis.getChance(), configOres.lapis.getBlacklist(), null, biomes, true);
            }
        }
        if (configOres.quartz.getChance() > 0)
        {
            // Standard world generation
            GeolosysAPI.registerMineralDeposit(oreVanillaMap.get(4), sampleVanillaMap.get(4), configOres.quartz);
        }
        if (configOres.kimberlite.getChance() > 0)
        {
            // Standard world generation
            GeolosysAPI.registerMineralDeposit(oreVanillaMap.get(5), sampleVanillaMap.get(5), configOres.kimberlite);
        }
        if (configOres.beryl.getChance() > 0)
        {
            if (configOres.teallite.getChance() <= 0 && configOres.cassiterite.getChance() <= 0)
            {
                // If not combined with Tin, then generate in the mountains
                ArrayList<Biome> biomes = new ArrayList<>();
                biomes.add(Biomes.ICE_MOUNTAINS);
                biomes.add(Biomes.EXTREME_HILLS);
                biomes.add(Biomes.EXTREME_HILLS_EDGE);
                biomes.add(Biomes.EXTREME_HILLS_WITH_TREES);
                biomes.add(Biomes.MUTATED_EXTREME_HILLS);
                biomes.add(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES);
                GeolosysAPI.registerMineralDeposit(oreVanillaMap.get(6), sampleVanillaMap.get(6),
                        configOres.beryl.getMinY(), configOres.beryl.getMaxY(), configOres.beryl.getSize(),
                        configOres.beryl.getChance(), configOres.beryl.getBlacklist(), null, biomes, true);
            }
        }
        if (ModConfig.featureControl.modStones)
        {
            IBlockState diorite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT,
                    BlockStone.EnumType.DIORITE);
            IBlockState andesite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT,
                    BlockStone.EnumType.ANDESITE);
            IBlockState granite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT,
                    BlockStone.EnumType.GRANITE);

            GeolosysAPI.registerStoneDeposit(andesite, 2, 70, 40);
            GeolosysAPI.registerStoneDeposit(diorite, 2, 70, 40);
            GeolosysAPI.registerStoneDeposit(granite, 2, 70, 40);
        }

        // Modded World Generation:
        if (configOres.hematite.getChance() > 0)
        {
            // Standard World Generation
            GeolosysAPI.registerMineralDeposit(oreMap.get(0), sampleMap.get(0), configOres.hematite);
        }
        if (configOres.limonite.getChance() > 0)
        {
            // Only generate in swamp biomes
            ArrayList<Biome> biomes = new ArrayList<>();
            biomes.add(Biomes.SWAMPLAND);
            biomes.add(Biomes.MUTATED_SWAMPLAND);
            biomes.add(Biomes.MUSHROOM_ISLAND);
            biomes.add(Biomes.MUSHROOM_ISLAND_SHORE);
            GeolosysAPI.registerMineralDeposit(oreMap.get(1), sampleMap.get(1), configOres.limonite.getMinY(),
                    configOres.limonite.getMaxY(), configOres.limonite.getSize(), configOres.limonite.getChance(),
                    configOres.limonite.getBlacklist(), null, biomes, true);
        }

        if (configOres.malachite.getChance() > 0 && configOres.azurite.getChance() > 0)
        {
            // Averages a combo of them all
            HashMap<IBlockState, Integer> oreBlocks = new HashMap<>();
            oreBlocks.put(oreMap.get(2), 40);
            oreBlocks.put(oreMap.get(3), 60);
            HashMap<IBlockState, Integer> sampleBlocks = new HashMap<>();
            sampleBlocks.put(sampleMap.get(2), 40);
            sampleBlocks.put(sampleMap.get(3), 60);
            GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks,
                    average(configOres.malachite.getMinY(), configOres.azurite.getMinY()),
                    average(configOres.malachite.getMaxY(), configOres.azurite.getMaxY()),
                    average(configOres.malachite.getSize(), configOres.azurite.getSize()),
                    average(configOres.malachite.getChance(), configOres.azurite.getChance()),
                    join(configOres.malachite.getBlacklist(), configOres.azurite.getBlacklist()), null);
        }
        else if (configOres.malachite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(oreMap.get(2), sampleMap.get(2), configOres.malachite);
        }
        else if (configOres.azurite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(oreMap.get(3), sampleMap.get(3), configOres.azurite);
        }
        if (configOres.cassiterite.getChance() > 0)
        {
            ArrayList<Biome> biomes = new ArrayList<Biome>();
            biomes.add(Biomes.OCEAN);
            biomes.add(Biomes.DEEP_OCEAN);
            biomes.add(Biomes.FROZEN_OCEAN);
            GeolosysAPI.registerMineralDeposit(oreMap.get(4), sampleMap.get(4), configOres.cassiterite.getMinY(),
                    configOres.cassiterite.getMaxY(), configOres.cassiterite.getSize(),
                    configOres.cassiterite.getChance(), configOres.cassiterite.getBlacklist(), null, biomes, true);
        }
        if (configOres.teallite.getChance() > 0)
        {
            HashMap<IBlockState, Integer> oreBlocks = new HashMap<>();
            oreBlocks.put(oreMap.get(5), 90);
            oreBlocks.put(oreVanillaMap.get(6), 10);
            HashMap<IBlockState, Integer> sampleBlocks = new HashMap<>();
            sampleBlocks.put(sampleMap.get(5), 90);
            sampleBlocks.put(sampleVanillaMap.get(6), 10);
            ArrayList<Biome> biomes = new ArrayList<Biome>();
            biomes.add(Biomes.OCEAN);
            biomes.add(Biomes.DEEP_OCEAN);
            biomes.add(Biomes.FROZEN_OCEAN);
            GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks, configOres.teallite.getMinY(),
                    configOres.teallite.getMaxY(), configOres.teallite.getSize(), configOres.teallite.getChance(),
                    configOres.teallite.getBlacklist(), null, biomes, true);
        }
        if (configOres.galena.getChance() > 0)
        {
            ArrayList<Biome> biomes = new ArrayList<Biome>();
            biomes.add(Biomes.PLAINS);
            biomes.add(Biomes.ICE_PLAINS);
            biomes.add(Biomes.MUTATED_PLAINS);
            GeolosysAPI.registerMineralDeposit(oreMap.get(6), sampleMap.get(6), configOres.galena.getMinY(),
                    configOres.galena.getMaxY(), configOres.galena.getSize(), configOres.galena.getChance(),
                    configOres.galena.getBlacklist(), null, biomes, true);
        }
        if (configOres.bauxite.getChance() > 0)
        {
            ArrayList<Biome> biomes = new ArrayList<Biome>();
            biomes.add(Biomes.JUNGLE);
            biomes.add(Biomes.JUNGLE_EDGE);
            biomes.add(Biomes.JUNGLE_HILLS);
            biomes.add(Biomes.MUTATED_JUNGLE);
            biomes.add(Biomes.MUTATED_JUNGLE_EDGE);
            biomes.add(Biomes.BEACH);
            biomes.add(Biomes.SAVANNA);
            biomes.add(Biomes.MUTATED_SAVANNA);
            biomes.add(Biomes.SAVANNA_PLATEAU);
            biomes.add(Biomes.MUTATED_SAVANNA_ROCK);
            GeolosysAPI.registerMineralDeposit(oreMap.get(7), sampleMap.get(7), configOres.bauxite.getMinY(),
                    configOres.bauxite.getMaxY(), configOres.bauxite.getSize(), configOres.bauxite.getChance(),
                    configOres.bauxite.getBlacklist(), null, biomes, true);
        }
        if (configOres.platinum.getChance() > 0)
        {
            ArrayList<Biome> biomes = new ArrayList<Biome>();
            biomes.add(Biomes.ICE_MOUNTAINS);
            biomes.add(Biomes.EXTREME_HILLS);
            biomes.add(Biomes.EXTREME_HILLS_EDGE);
            biomes.add(Biomes.EXTREME_HILLS_WITH_TREES);
            biomes.add(Biomes.MUTATED_EXTREME_HILLS);
            biomes.add(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES);
            GeolosysAPI.registerMineralDeposit(oreMap.get(8), sampleMap.get(8), configOres.platinum.getMinY(),
                    configOres.platinum.getMaxY(), configOres.platinum.getSize(), configOres.platinum.getChance(),
                    configOres.platinum.getBlacklist(), null, biomes, true);
        }
        if (configOres.autunite.getChance() > 0)
        {
            ArrayList<IBlockState> matchers = new ArrayList<>();
            matchers.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE));
            HashMap<IBlockState, Integer> oreBlocks = new HashMap<>();
            oreBlocks.put(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE),
                    15);
            oreBlocks.put(oreMap.get(9), 85);
            HashMap<IBlockState, Integer> sampleBlocks = new HashMap<>();
            sampleBlocks.put(sampleMap.get(9), 100);
            ArrayList<Biome> biomes = new ArrayList<Biome>();
            biomes.add(Biomes.ICE_MOUNTAINS);
            biomes.add(Biomes.EXTREME_HILLS);
            biomes.add(Biomes.EXTREME_HILLS_EDGE);
            biomes.add(Biomes.EXTREME_HILLS_WITH_TREES);
            biomes.add(Biomes.MUTATED_EXTREME_HILLS);
            biomes.add(Biomes.MUTATED_EXTREME_HILLS_WITH_TREES);
            GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks, configOres.autunite.getMinY(),
                    configOres.autunite.getMaxY(), configOres.autunite.getSize(), configOres.autunite.getChance(),
                    configOres.autunite.getBlacklist(), matchers, biomes, true);
        }
        if (configOres.sphalerite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(oreMap.get(10), sampleMap.get(10), configOres.sphalerite);
        }
    }

    private static int average(int... inputs)
    {
        int sum = 0;
        for (int input : inputs)
        {
            sum += input;
        }
        return sum / inputs.length;
    }

    private static int[] join(int[]... inputs)
    {
        int size = 0;
        for (int[] input : inputs)
        {
            size += input.length;
        }
        int[] ret = new int[size];
        int x = 0;
        for (int[] input : inputs)
        {
            for (int i : input)
            {
                ret[x] = i;
                x++;
            }
        }
        return ret;
    }
}