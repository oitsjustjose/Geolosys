package com.oitsjustjose.geolosys.common.data.serializer;

import java.lang.reflect.Type;
import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.DepositStone;

import net.minecraft.block.BlockState;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class StoneConfigSerializer implements JsonDeserializer<DepositStone>, JsonSerializer<DepositStone> {
    public DepositStone deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) {
        JsonObject jsonobject = JSONUtils.getJsonObject(json, "config");

        if (jsonobject == null) {
            return null;
        }

        try {
            BlockState block = fromString(jsonobject.get("block").getAsString());
            int size = jsonobject.get("size").getAsInt();
            int chance = jsonobject.get("chance").getAsInt();
            int yMin = jsonobject.get("yMin").getAsInt();
            int yMax = jsonobject.get("yMax").getAsInt();
            String[] dimFilter = Utility.getDimFilter(jsonobject);
            boolean isDimFilterBl = Utility.getIsDimFilterBl(jsonobject);

            return new DepositStone(block, yMin, yMax, chance, size, dimFilter, isDimFilterBl);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse JSON file: {}", e);
            return null;
        }
    }

    public JsonElement serialize(DepositStone dep, Type type, JsonSerializationContext ctx) {
        JsonObject json = new JsonObject();
        JsonObject config = new JsonObject();
        JsonParser parser = new JsonParser();

        config.addProperty("block", dep.getOre().getBlock().getRegistryName().toString());
        config.addProperty("size", dep.getSize());
        config.addProperty("chance", dep.getChance());
        config.addProperty("yMin", dep.getYMin());
        config.addProperty("yMax", dep.getYMax());
        config.add("dimBlacklist", parser.parse(Arrays.toString(dep.getDimensionFilter())));

        json.addProperty("type", "geolosys:stone_deposit");
        json.add("config", config);

        return json;
    }

    @Nullable
    private BlockState fromString(String string) {
        ResourceLocation r = new ResourceLocation(string);
        return ForgeRegistries.BLOCKS.getValue(r).getDefaultState();
    }
}
