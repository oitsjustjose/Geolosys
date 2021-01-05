package com.oitsjustjose.geolosys.common.config.serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.PlutonType;
import com.oitsjustjose.geolosys.api.world.Deposit;
import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class OreConfigSerializer implements JsonDeserializer<IDeposit>, JsonSerializer<IDeposit> {
    public IDeposit deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) {
        JsonObject jsonobject = JSONUtils.getJsonObject(json, "config");

        if (jsonobject == null) {
            return null;
        }

        try {
            if (jsonobject.has("blocks") && jsonobject.has("samples")) {
                if (jsonobject.has("biomes")) {
                    return deserializeDepositMultiOreBiomeRestricted(jsonobject);
                }
                return deserializeDepositMultiOre(jsonobject);
            } else if (jsonobject.has("biomes")) {
                return deserializeDepositBiomeRestricted(jsonobject);
            } else if (jsonobject.has("block") && jsonobject.has("sample")) {
                return deserializeDeposit(jsonobject);
            }

            Geolosys.getInstance().LOGGER.error("Given JSON file has a mix of 'block(s)' and 'sample(s)'");
            return null;
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse JSON file");
            return null;
        }
    }

    public JsonElement serialize(IDeposit dep, Type type, JsonSerializationContext ctx) {
        JsonObject json = new JsonObject();
        JsonObject config = new JsonObject();
        JsonParser parser = new JsonParser();

        // Add the base set
        config.addProperty("size", dep.getSize());
        config.addProperty("chance", dep.getChance());
        config.addProperty("yMin", dep.getYMin());
        config.addProperty("yMax", dep.getYMax());
        config.addProperty("type", dep.getPlutonType().name());
        config.addProperty("density", dep.getDensity());
        config.add("dimBlacklist", parser.parse(Arrays.toString(dep.getDimensionBlacklist())));

        // Now add extras
        if (dep instanceof DepositMultiOre) {
            DepositMultiOre dmo = (DepositMultiOre) dep;
            config.add("blocks", deconstructMultiBlockMap(dmo.oreBlocks));
            config.add("samples", deconstructMultiBlockMap(dmo.sampleBlocks));
        } else {
            config.addProperty("block", dep.getOre().getBlock().getRegistryName().toString());
            config.addProperty("sample", dep.getSample().getBlock().getRegistryName().toString());
        }

        if (dep instanceof DepositBiomeRestricted || dep instanceof DepositMultiOreBiomeRestricted) {
            DepositBiomeRestricted dbr = (DepositBiomeRestricted) dep;
            config.add("biomes", deconstructBiomes(dbr.getBiomeList(), dbr.getBiomeTypes()));
            config.addProperty("isWhitelist", dbr.useWhitelist());
        }

        json.addProperty("type", "geolosys:ore_deposit");
        json.add("config", config);

        return json;
    }

    private Deposit deserializeDeposit(JsonObject json) {
        try {
            BlockState block = fromString(json.get("block").getAsString());
            BlockState sample = fromString(json.get("sample").getAsString());
            int size = json.get("size").getAsInt();
            int chance = json.get("chance").getAsInt();
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            String[] dimBlacklist = toStringArray(json.get("dimBlackist").getAsJsonArray());
            List<BlockState> blockStateMatchers = Utils.getDefaultMatchers();
            PlutonType type = PlutonType.valueOf(json.get("type").getAsString());
            float density = json.get("density").getAsFloat();
            if (json.has("blockStateMatchers")) {
                blockStateMatchers = toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new Deposit(block, sample, yMin, yMax, chance, size, dimBlacklist, blockStateMatchers, type,
                    density);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse JSON file");
            return null;
        }
    }

    private DepositMultiOre deserializeDepositMultiOre(JsonObject json) {
        try {
            HashMap<BlockState, Integer> blocks = buildMultiBlockMap(json.get("blocks").getAsJsonArray());
            HashMap<BlockState, Integer> samples = buildMultiBlockMap(json.get("samples").getAsJsonArray());
            int size = json.get("size").getAsInt();
            int chance = json.get("chance").getAsInt();
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            String[] dimBlacklist = toStringArray(json.get("dimBlackist").getAsJsonArray());
            List<BlockState> blockStateMatchers = Utils.getDefaultMatchers();
            PlutonType type = PlutonType.valueOf(json.get("type").getAsString());
            float density = json.get("density").getAsFloat();
            if (json.has("blockStateMatchers")) {
                blockStateMatchers = toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new DepositMultiOre(blocks, samples, yMin, yMax, chance, size, dimBlacklist, blockStateMatchers,
                    type, density);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse JSON file");
            return null;
        }
    }

    private DepositBiomeRestricted deserializeDepositBiomeRestricted(JsonObject json) {
        try {
            BlockState block = fromString(json.get("block").getAsString());
            BlockState sample = fromString(json.get("sample").getAsString());
            int size = json.get("size").getAsInt();
            int chance = json.get("chance").getAsInt();
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            String[] dimBlacklist = toStringArray(json.get("dimBlackist").getAsJsonArray());
            List<BlockState> blockStateMatchers = Utils.getDefaultMatchers();
            PlutonType type = PlutonType.valueOf(json.get("type").getAsString());
            float density = json.get("density").getAsFloat();
            boolean isWhitelist = json.get("isWhitelist").getAsBoolean();
            List<BiomeDictionary.Type> biomeTypes = extractBiomeTypes(json.get("biomes").getAsJsonArray());
            List<Biome> biomes = extractBiomes(json.get("biomes").getAsJsonArray());

            if (json.has("blockStateMatchers")) {
                blockStateMatchers = toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new DepositBiomeRestricted(block, sample, yMin, yMax, chance, size, dimBlacklist, blockStateMatchers,
                    biomes, biomeTypes, isWhitelist, type, density);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse JSON file");
            return null;
        }
    }

    private DepositMultiOreBiomeRestricted deserializeDepositMultiOreBiomeRestricted(JsonObject json) {
        try {
            HashMap<BlockState, Integer> blocks = buildMultiBlockMap(json.get("blocks").getAsJsonArray());
            HashMap<BlockState, Integer> samples = buildMultiBlockMap(json.get("samples").getAsJsonArray());
            int size = json.get("size").getAsInt();
            int chance = json.get("chance").getAsInt();
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            String[] dimBlacklist = toStringArray(json.get("dimBlackist").getAsJsonArray());
            List<BlockState> blockStateMatchers = Utils.getDefaultMatchers();
            PlutonType type = PlutonType.valueOf(json.get("type").getAsString());
            float density = json.get("density").getAsFloat();
            boolean isWhitelist = json.get("isWhitelist").getAsBoolean();
            List<BiomeDictionary.Type> biomeTypes = extractBiomeTypes(json.get("biomes").getAsJsonArray());
            List<Biome> biomes = extractBiomes(json.get("biomes").getAsJsonArray());

            if (json.has("blockStateMatchers")) {
                blockStateMatchers = toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new DepositMultiOreBiomeRestricted(blocks, samples, yMin, yMax, chance, size, dimBlacklist,
                    blockStateMatchers, biomes, biomeTypes, isWhitelist, type, density);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse JSON file");
            return null;
        }
    }

    @Nullable
    private BlockState fromString(String string) {
        ResourceLocation r = new ResourceLocation(string);
        return ForgeRegistries.BLOCKS.getValue(r).getDefaultState();
    }

    private String[] toStringArray(JsonArray arr) {
        String[] ret = new String[arr.size()];

        for (int i = 0; i < arr.size(); i++) {
            ret[i] = arr.get(i).getAsString();
        }

        return ret;
    }

    private List<BlockState> toBlockStateList(JsonArray arr) {
        List<BlockState> ret = new ArrayList<BlockState>();

        for (String s : toStringArray(arr)) {
            ret.add(fromString(s));
        }

        return ret;
    }

    private HashMap<BlockState, Integer> buildMultiBlockMap(JsonArray arr) {
        HashMap<BlockState, Integer> ret = new HashMap<BlockState, Integer>();

        for (JsonElement j : arr) {
            JsonObject pair = j.getAsJsonObject();
            ret.put(fromString(pair.get("block").getAsString()), pair.get("chance").getAsInt());
        }

        return ret;
    }

    private JsonArray deconstructMultiBlockMap(HashMap<BlockState, Integer> in) {
        JsonArray ret = new JsonArray();

        for (Entry<BlockState, Integer> e : in.entrySet()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("block", e.getKey().getBlock().getRegistryName().toString());
            obj.addProperty("chance", e.getValue());
            ret.add(obj);
        }

        return ret;
    }

    private List<BiomeDictionary.Type> extractBiomeTypes(JsonArray arr) {
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

    private List<Biome> extractBiomes(JsonArray arr) {
        List<Biome> ret = new ArrayList<Biome>();

        for (String s : toStringArray(arr)) {
            ResourceLocation r = new ResourceLocation(s);
            if (ForgeRegistries.BIOMES.containsKey(r)) {
                ret.add(ForgeRegistries.BIOMES.getValue(r));
            }
        }

        return ret;
    }

    private JsonArray deconstructBiomes(List<Biome> biomes, List<BiomeDictionary.Type> types) {
        JsonArray ret = new JsonArray();

        for (Biome b : biomes) {
            ret.add(b.getRegistryName().toString());
        }

        for (BiomeDictionary.Type t : types) {
            ret.add(t.getName());
        }

        return ret;
    }
}
