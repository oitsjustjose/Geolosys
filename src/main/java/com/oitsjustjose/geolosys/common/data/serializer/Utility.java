package com.oitsjustjose.geolosys.common.data.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class Utility {
    public static String[] getDimFilter(JsonObject obj) {
        if (obj.has("dimensions") && obj.get("dimensions").isJsonObject()) {
            return toStringArray(obj.get("dimensions").getAsJsonObject().get("filter").getAsJsonArray());
        }
        return toStringArray(obj.get("dimBlacklist").getAsJsonArray());
    }

    public static boolean getIsDimFilterBl(JsonObject obj) {
        if (obj.has("dimensions") && obj.get("dimensions").isJsonObject()) {
            return obj.get("dimensions").getAsJsonObject().get("isBlacklist").getAsBoolean();
        }
        return true;
    }

    public static JsonArray deconstructBiomes(List<Biome> biomes, List<BiomeDictionary.Type> types) {
        JsonArray ret = new JsonArray();

        for (Biome b : biomes) {
            ret.add(b.getRegistryName().toString());
        }

        for (BiomeDictionary.Type t : types) {
            ret.add(t.getName());
        }

        return ret;
    }

    @Nullable
    public static BlockState fromString(String string) {
        ResourceLocation r = new ResourceLocation(string);
        return ForgeRegistries.BLOCKS.getValue(r).getDefaultState();
    }

    public static String[] toStringArray(JsonArray arr) {
        String[] ret = new String[arr.size()];

        for (int i = 0; i < arr.size(); i++) {
            ret[i] = arr.get(i).getAsString();
        }

        return ret;
    }

    public static HashSet<BlockState> toBlockStateList(JsonArray arr) {
        HashSet<BlockState> ret = new HashSet<BlockState>();

        for (String s : toStringArray(arr)) {
            ret.add(fromString(s));
        }

        return ret;
    }

    public static HashMap<BlockState, Integer> buildMultiBlockMap(JsonArray arr) {
        HashMap<BlockState, Integer> ret = new HashMap<BlockState, Integer>();

        for (JsonElement j : arr) {
            JsonObject pair = j.getAsJsonObject();
            ret.put(fromString(pair.get("block").getAsString()), pair.get("chance").getAsInt());
        }

        return ret;
    }

    public static JsonArray deconstructMultiBlockMap(HashMap<BlockState, Integer> in) {
        JsonArray ret = new JsonArray();

        for (Entry<BlockState, Integer> e : in.entrySet()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("block", e.getKey().getBlock().getRegistryName().toString());
            obj.addProperty("chance", e.getValue());
            ret.add(obj);
        }

        return ret;
    }

    public static List<BiomeDictionary.Type> extractBiomeTypes(JsonArray arr) {
        List<BiomeDictionary.Type> ret = new ArrayList<BiomeDictionary.Type>();

        for (BiomeDictionary.Type biomeType : BiomeDictionary.Type.getAll()) {
            for (String type : toStringArray(arr)) {
                if (biomeType.getName().equalsIgnoreCase(type)) {
                    if (!ret.contains(biomeType)) {
                        ret.add(biomeType);
                    }
                    break;
                }
            }
        }

        return ret;
    }

    public static List<Biome> extractBiomes(JsonArray arr) {
        List<Biome> ret = new ArrayList<Biome>();

        for (String s : toStringArray(arr)) {
            ResourceLocation r = new ResourceLocation(s.toLowerCase());
            if (ForgeRegistries.BIOMES.containsKey(r)) {
                ret.add(ForgeRegistries.BIOMES.getValue(r));
            }
        }

        return ret;
    }
}
