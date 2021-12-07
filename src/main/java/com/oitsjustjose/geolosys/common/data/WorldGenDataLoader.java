package com.oitsjustjose.geolosys.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.deposit.*;
import com.oitsjustjose.geolosys.common.data.serializer.*;
import com.oitsjustjose.geolosys.common.utils.Prospecting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import javax.annotation.Nonnull;
import java.util.Map;

public class WorldGenDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private DenseDepositSerializer denseDepSer = new DenseDepositSerializer();
    private LayerDepositSerializer layerDepSer = new LayerDepositSerializer();
    private TopLayerDepositSerializer topLayerDepSer = new TopLayerDepositSerializer();
    private DikeDepositSerializer dikeDepSer = new DikeDepositSerializer();
    private SparseDepositSerializer sparseDepSer = new SparseDepositSerializer();

    public WorldGenDataLoader() {
        super(GSON, "deposits");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> datamap, @Nonnull ResourceManager manager,
            ProfilerFiller profiler) {
        GeolosysAPI.plutonRegistry.clear();
        datamap.forEach((rl, json) -> {
            try {
                JsonObject jsonobject = json.getAsJsonObject();
                JsonObject config = jsonobject.get("config").getAsJsonObject();
                Geolosys.getInstance().LOGGER.info("Preparing to load deposit datafile {}", rl.toString());

                switch (jsonobject.get("type").getAsString()) {
                    case "geolosys:deposit_dense":
                        DenseDeposit denseDeposit = denseDepSer.deserialize(config, null);
                        if (denseDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(denseDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(denseDeposit);
                        }
                        return;
                    case "geolosys:deposit_layer":
                        LayerDeposit layerDeposit = layerDepSer.deserialize(config, null);
                        if (layerDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(layerDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(layerDeposit);
                        }
                        return;
                    case "geolosys:deposit_top_layer":
                        TopLayerDeposit topLayerDeposit = topLayerDepSer.deserialize(config, null);
                        if (topLayerDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(topLayerDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(topLayerDeposit);
                        }
                        return;
                    case "geolosys:deposit_dike":
                        DikeDeposit dikeDeposit = dikeDepSer.deserialize(config, null);
                        if (dikeDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(dikeDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(dikeDeposit);
                        }
                        return;
                    case "geolosys:deposit_sparse":
                        SparseDeposit sparseDeposit = sparseDepSer.deserialize(config, null);
                        if (sparseDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(sparseDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(sparseDeposit);
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

        Prospecting.populateDepositBlocks();
    }
}
