package com.oitsjustjose.geolosys.common.data.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.common.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Objects;

public class SerializerUtils {

    public static @NotNull BlockState fromString(@Nullable String string) {
        if (string == null) {
            return Blocks.AIR.defaultBlockState();
        }
        ResourceLocation r = new ResourceLocation(string);
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(r)).defaultBlockState();
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

    public static HashMap<BlockState, Float> buildMultiBlockMap(JsonArray arr) {
        HashMap<BlockState, Float> ret = new HashMap<BlockState, Float>();

        for (JsonElement j : arr) {
            JsonObject pair = j.getAsJsonObject();
            if (pair.get("block").isJsonNull()) {
                ret.put(null, pair.get("chance").getAsFloat());
            } else {
                ret.put(fromString(pair.get("block").getAsString()), pair.get("chance").getAsFloat());
            }
        }

        return ret;
    }

    public static JsonArray deconstructMultiBlockMap(HashMap<BlockState, Float> in) {
        JsonArray ret = new JsonArray();

        for (Entry<BlockState, Float> e : in.entrySet()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("block", Utils.getRegistryName(e.getKey().getBlock()));
            obj.addProperty("chance", e.getValue());
            ret.add(obj);
        }

        return ret;
    }

    public static HashMap<String, HashMap<BlockState, Float>> buildMultiBlockMatcherMap(JsonObject obj) {
        HashMap<String, HashMap<BlockState, Float>> ret = new HashMap<>();

        obj.keySet().forEach((key) -> {
            HashMap<BlockState, Float> value = buildMultiBlockMap(obj.get(key).getAsJsonArray());
            ret.put(key, value);
        });

        return ret;
    }

    public static JsonObject deconstructMultiBlockMatcherMap(HashMap<String, HashMap<BlockState, Float>> in) {
        JsonObject ret = new JsonObject();

        for (Entry<String, HashMap<BlockState, Float>> i : in.entrySet()) {
            String key = i.getKey();
            JsonArray value = deconstructMultiBlockMap(i.getValue());
            ret.add(key, value);
        }

        return ret;
    }
}
