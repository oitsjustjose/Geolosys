package com.oitsjustjose.geolosys.common.data;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.deposit.DenseDeposit;
import com.oitsjustjose.geolosys.api.world.deposit.DikeDeposit;
import com.oitsjustjose.geolosys.api.world.deposit.LayerDeposit;
import com.oitsjustjose.geolosys.common.data.serializer.DenseDepositSerializer;
import com.oitsjustjose.geolosys.common.data.serializer.DikeDepositSerializer;
import com.oitsjustjose.geolosys.common.data.serializer.LayerDepositSerializer;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class WorldGenDataLoader extends JsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private DenseDepositSerializer denseDepSer = new DenseDepositSerializer();
    private LayerDepositSerializer layerDepSer = new LayerDepositSerializer();
    private DikeDepositSerializer dikeDepSer = new DikeDepositSerializer();

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
                    case "geolosys:ore_deposit_dense":
                        DenseDeposit denseDeposit = denseDepSer.deserialize(config, null);
                        if (denseDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(denseDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(denseDeposit);
                        }
                        return;
                    case "geolosys:ore_deposit_layer":
                        LayerDeposit layerDeposit = layerDepSer.deserialize(config, null);
                        if (layerDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(layerDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(layerDeposit);
                        }
                        return;
                    case "geolosys:ore_deposit_dike":
                        DikeDeposit dikeDeposit = dikeDepSer.deserialize(config, null);
                        if (dikeDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(dikeDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(dikeDeposit);
                        }
                        return;
                    default:
                        Geolosys.getInstance().LOGGER.warn("Unknown JSON type. Received JSON {}", json.toString());
                        return;
                }
            } catch (NullPointerException ex) {
                Geolosys.getInstance().LOGGER.info("Skipping registration of ore {}", rl);
            }
        });
    }
}
