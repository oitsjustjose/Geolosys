package com.oitsjustjose.geolosys.common.data;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.DepositStone;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.data.serializer.OreConfigSerializer;
import com.oitsjustjose.geolosys.common.data.serializer.StoneConfigSerializer;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class WorldGenDataLoader extends JsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private OreConfigSerializer oreSerializer = new OreConfigSerializer();
    private StoneConfigSerializer stoneSerializer = new StoneConfigSerializer();

    public WorldGenDataLoader() {
        super(GSON, "deposits");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> datamap, @Nonnull IResourceManager manager,
            IProfiler profiler) {
        GeolosysAPI.plutonRegistry.clear();
        datamap.forEach((rl, json) -> {
            try {
                JsonObject jsonobject = JSONUtils.getJsonObject(json, "geolosys deposit config");
                JsonObject config = jsonobject.get("config").getAsJsonObject();

                switch (jsonobject.get("type").getAsString()) {
                    case "geolosys:ore_deposit":
                        IDeposit dep = oreSerializer.deserialize(config, null, null);
                        if (dep != null) {
                            Utils.logDeposit(dep);
                            GeolosysAPI.plutonRegistry.addOrePluton(dep);
                        }
                        return;
                    case "geolosys:stone_deposit":
                        DepositStone stone = stoneSerializer.deserialize(config, null, null);
                        if (stone != null) {
                            Utils.logDeposit(stone);
                            GeolosysAPI.plutonRegistry.addStonePluton(stone);
                        }
                        break;
                    default:
                        Geolosys.getInstance().LOGGER.info("Unknown JSON type. Received JSON {}", json.toString());
                        return;
                }
            } catch (NullPointerException ex) {
                Geolosys.getInstance().LOGGER.info("Skipping registration of ore {}", rl);
            }
        });
    }
}
