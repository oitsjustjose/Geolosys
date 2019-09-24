package com.oitsjustjose.geolosys.common.world.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.oitsjustjose.geolosys.api.world.Deposit;
import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositStone;
import com.oitsjustjose.geolosys.api.world.IOre;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class PlutonFeatureConfig implements IFeatureConfig
{
    private IOre pluton;

    public PlutonFeatureConfig(IOre plutonIn)
    {
        this.pluton = plutonIn;
    }

    public IOre getPluton()
    {
        return this.pluton;
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops)
    {
        Map<T, T> params = new HashMap<>();
        params.put(ops.createString("yMin"), ops.createInt(this.pluton.getYMin()));
        params.put(ops.createString("yMax"), ops.createInt(this.pluton.getYMax()));
        params.put(ops.createString("size"), ops.createInt(this.pluton.getSize()));
        params.put(ops.createString("chance"), ops.createInt(this.pluton.getChance()));
        params.put(ops.createString("density"), ops.createFloat(this.pluton.getDensity()));
        params.put(ops.createString("dimensionBlacklist"),
                ops.createIntList(IntStream.of(this.pluton.getDimensionBlacklist())));
        params.put(ops.createString("blockStateMatchers"), ops.createList(
                this.pluton.getBlockStateMatchers().stream().map(s -> BlockState.serialize(ops, s).getValue())));

        // So this sucks but we're gonna have to write out our serialization using all this generic crap
        if (this.pluton instanceof DepositMultiOre)
        {
            DepositMultiOre temp = (DepositMultiOre) pluton;
            Map<T, T> oreMap = new HashMap<>();
            Map<T, T> sampleMap = new HashMap<>();
            for (BlockState state : temp.oreBlocks.keySet())
            {
                oreMap.put(BlockState.serialize(ops, state).getValue(), ops.createInt(temp.oreBlocks.get(state)));
            }
            for (BlockState state : temp.sampleBlocks.keySet())
            {
                sampleMap.put(BlockState.serialize(ops, state).getValue(), ops.createInt(temp.oreBlocks.get(state)));
            }

            params.put(ops.createString("type"), ops.createString("DepositMultiOre"));
            params.put(ops.createString("ores"), ops.createMap(oreMap));
            params.put(ops.createString("samples"), ops.createMap(sampleMap));

        }
        else if (this.pluton instanceof DepositMultiOreBiomeRestricted)
        {
            DepositMultiOreBiomeRestricted temp = (DepositMultiOreBiomeRestricted) this.pluton;

            Map<T, T> oreMap = new HashMap<>();
            Map<T, T> sampleMap = new HashMap<>();
            for (BlockState state : temp.oreBlocks.keySet())
            {
                oreMap.put(BlockState.serialize(ops, state).getValue(), ops.createInt(temp.oreBlocks.get(state)));
            }
            for (BlockState state : temp.sampleBlocks.keySet())
            {
                sampleMap.put(BlockState.serialize(ops, state).getValue(), ops.createInt(temp.oreBlocks.get(state)));
            }

            params.put(ops.createString("type"), ops.createString("DepositMultiOreBiomeRestricted"));
            params.put(ops.createString("ores"), ops.createMap(oreMap));
            params.put(ops.createString("samples"), ops.createMap(sampleMap));
            // To parse this back use BiomeDictionary.Type.getType(<V>)
            params.put(ops.createString("biomeTypes"),
                    ops.createList(temp.getBiomeTypes().stream().map(b -> ops.createString(b.getName()))));
            // To parse this back use ForgeRegistries.BIOMES.getValue(new ResourceLocation(<V>))
            params.put(ops.createString("biomes"), ops.createList(
                    temp.getBiomeList().stream().map(b -> ops.createString(b.getRegistryName().toString()))));
            params.put(ops.createString("useWhitelist"), ops.createBoolean(temp.useWhitelist()));
        }
        else if (this.pluton instanceof DepositBiomeRestricted)
        {
            DepositBiomeRestricted temp = (DepositBiomeRestricted) this.pluton;

            params.put(ops.createString("type"), ops.createString("DepositBiomeRestricted"));
            params.put(ops.createString("ore"), BlockState.serialize(ops, temp.getOre()).getValue());
            params.put(ops.createString("sample"), BlockState.serialize(ops, temp.getSample()).getValue());
            params.put(ops.createString("biomeTypes"),
                    ops.createList(temp.getBiomeTypes().stream().map(b -> ops.createString(b.getName()))));
            params.put(ops.createString("biomes"), ops.createList(
                    temp.getBiomeList().stream().map(b -> ops.createString(b.getRegistryName().toString()))));
            params.put(ops.createString("useWhitelist"), ops.createBoolean(temp.useWhitelist()));
        }

        else if (this.pluton instanceof DepositStone)
        {
            DepositStone temp = (DepositStone) this.pluton;

            params.put(ops.createString("type"), ops.createString("DepositStone"));
            params.put(ops.createString("ore"), BlockState.serialize(ops, temp.getOre()).getValue());
        }
        else
        {
            params.put(ops.createString("type"), ops.createString("Deposit"));
            params.put(ops.createString("ore"), BlockState.serialize(ops, this.pluton.getOre()).getValue());
            params.put(ops.createString("sample"), BlockState.serialize(ops, this.pluton.getSample()).getValue());
        }

        return new Dynamic<>(ops, ops.createMap(params));
    }

    public static PlutonFeatureConfig deserialize(Dynamic<?> in)
    {
        IOre ret = null;

        String type = in.get("type").asString("Deposit");
        int yMin = in.get("yMin").asInt(0);
        int yMax = in.get("yMax").asInt(0);
        int size = in.get("size").asInt(0);
        int chance = in.get("chance").asInt(0);
        float density = in.get("density").asFloat(0.0F);
        int[] dimBlacklist = in.get("dimensionBlacklist").asIntStream().toArray();
        // I can't believe I figured out this crazy bullshit.
        // Maybe my Master's Degree in CS is helping
        List<BlockState> blockStateMatchers = in.get("blockStateMatchers").asStream().map(BlockState::deserialize)
                .collect(Collectors.toList());

        if (type.equalsIgnoreCase("DepositMultiOre"))
        {
            /*
             * Ok, below code deserves an explanation
             * 
             * The HashMaps are what the new DepositMultiOre **REQUIRES** Due to some weirdness with the .asMap returning a value that
             * doesn't work (??) for HashMap, I have to conform it into the HashMap manually. So what you end up with is a Map<BlockState,
             * Integer> whose keypairs I iterate through using a map, in which the pair.getKey(), pair.getValue() are putinto the HashMap
             * forms that I need.
             */
            HashMap<BlockState, Integer> blockMap = new HashMap<>();
            HashMap<BlockState, Integer> sampleMap = new HashMap<>();
            Map<BlockState, Integer> tempBlockMap = in.get("ores").asMap(BlockState::deserialize, x -> x.asInt(0));
            tempBlockMap.entrySet().stream().map(pair -> blockMap.put(pair.getKey(), pair.getValue()));
            Map<BlockState, Integer> tempSampleMap = in.get("samples").asMap(BlockState::deserialize, x -> x.asInt(0));
            tempSampleMap.entrySet().stream().map(pair -> sampleMap.put(pair.getKey(), pair.getValue()));

            ret = new DepositMultiOre(blockMap, sampleMap, yMin, yMax, size, chance, dimBlacklist, blockStateMatchers,
                    density);
        }
        else if (type.equalsIgnoreCase("DepositMultiOreBiomeRestricted"))
        {
            /* See above for an explanation on the madness between this and the next comment */
            HashMap<BlockState, Integer> blockMap = new HashMap<>();
            HashMap<BlockState, Integer> sampleMap = new HashMap<>();
            Map<BlockState, Integer> tempBlockMap = in.get("ores").asMap(BlockState::deserialize, x -> x.asInt(0));
            tempBlockMap.entrySet().stream().map(pair -> blockMap.put(pair.getKey(), pair.getValue()));
            Map<BlockState, Integer> tempSampleMap = in.get("samples").asMap(BlockState::deserialize, x -> x.asInt(0));
            tempSampleMap.entrySet().stream().map(pair -> sampleMap.put(pair.getKey(), pair.getValue()));
            /* Maps the stream of strings back into a List of BiomeTypes using the Type.getType(str) function */
            List<BiomeDictionary.Type> biomeTypes = in.get("biomeTypes").asStream()
                    .map(x -> BiomeDictionary.Type.getType(x.asString(""))).collect(Collectors.toList());
            /* Maps the stream of strings back into a List of Biomes by searching the ForgeRegistry for the resourcelocation */
            List<Biome> biomes = in.get("biomes").asStream()
                    .map(x -> ForgeRegistries.BIOMES.getValue(new ResourceLocation(x.asString(""))))
                    .collect(Collectors.toList());
            boolean useWhitelist = in.get("useWhitelist").asBoolean(false);

            ret = new DepositMultiOreBiomeRestricted(blockMap, sampleMap, yMin, yMax, size, chance, dimBlacklist,
                    blockStateMatchers, biomes, biomeTypes, useWhitelist, density);

        }
        else if (type.equalsIgnoreCase("DepositBiomeRestricted"))
        {
            BlockState ore = in.get("ore").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
            BlockState sample = in.get("sample").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
            /* Maps the stream of strings back into a List of BiomeTypes using the Type.getType(str) function */
            List<BiomeDictionary.Type> biomeTypes = in.get("biomeTypes").asStream()
                    .map(x -> BiomeDictionary.Type.getType(x.asString(""))).collect(Collectors.toList());
            /* Maps the stream of strings back into a List of Biomes by searching the ForgeRegistry for the resourcelocation */
            List<Biome> biomes = in.get("biomes").asStream()
                    .map(x -> ForgeRegistries.BIOMES.getValue(new ResourceLocation(x.asString(""))))
                    .collect(Collectors.toList());
            boolean useWhitelist = in.get("useWhitelist").asBoolean(false);

            ret = new DepositBiomeRestricted(ore, sample, yMin, yMax, size, chance, dimBlacklist, blockStateMatchers,
                    biomes, biomeTypes, useWhitelist, density);
        }
        else if (type.equalsIgnoreCase("DepositStone"))
        {
            BlockState ore = in.get("ore").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
            ret = new DepositStone(ore, yMin, yMax, chance, size, dimBlacklist);
        }
        else
        {
            BlockState ore = in.get("ore").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
            BlockState sample = in.get("sample").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
            ret = new Deposit(ore, sample, yMin, yMax, size, chance, dimBlacklist, blockStateMatchers, density);
        }
        return new PlutonFeatureConfig(ret);
    }
}