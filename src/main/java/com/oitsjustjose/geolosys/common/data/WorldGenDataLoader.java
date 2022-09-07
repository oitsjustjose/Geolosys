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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;

public class WorldGenDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final DenseDepositSerializer DenseSerializer = new DenseDepositSerializer();
    private final LayerDepositSerializer LayerSerializer = new LayerDepositSerializer();
    private final TopLayerDepositSerializer TopLayerSerializer = new TopLayerDepositSerializer();
    private final DikeDepositSerializer DikeSerializer = new DikeDepositSerializer();
    private final SparseDepositSerializer SparseSerializer = new SparseDepositSerializer();

    public WorldGenDataLoader() {
        super(GSON, "deposits");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> datamap, @Nonnull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        GeolosysAPI.plutonRegistry.clear();
        datamap.forEach((rl, json) -> {
            try {
                JsonObject jsonobject = json.getAsJsonObject();
                JsonObject config = jsonobject.get("config").getAsJsonObject();
                Geolosys.getInstance().LOGGER.info("Preparing to load deposit datafile {}", rl.toString());

                switch (jsonobject.get("type").getAsString()) {
                    case "geolosys:deposit_dense" -> {
                        DenseDeposit denseDeposit = DenseSerializer.deserialize(config);
                        if (denseDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(denseDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(denseDeposit);
                        }
                    }
                    case "geolosys:deposit_layer" -> {
                        LayerDeposit layerDeposit = LayerSerializer.deserialize(config);
                        if (layerDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(layerDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(layerDeposit);
                        }
                    }
                    case "geolosys:deposit_top_layer" -> {
                        TopLayerDeposit topLayerDeposit = TopLayerSerializer.deserialize(config);
                        if (topLayerDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(topLayerDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(topLayerDeposit);
                        }
                    }
                    case "geolosys:deposit_dike" -> {
                        DikeDeposit dikeDeposit = DikeSerializer.deserialize(config);
                        if (dikeDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(dikeDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(dikeDeposit);
                        }
                    }
                    case "geolosys:deposit_sparse" -> {
                        SparseDeposit sparseDeposit = SparseSerializer.deserialize(config);
                        if (sparseDeposit != null) {
                            Geolosys.getInstance().LOGGER.info(sparseDeposit.toString());
                            GeolosysAPI.plutonRegistry.addDeposit(sparseDeposit);
                        }
                    }
                    default -> {
                        Geolosys.getInstance().LOGGER.warn("Unknown JSON type. Received JSON {}", json.toString());
                    }
                }
            } catch (NullPointerException ex) {
                Geolosys.getInstance().LOGGER.info("Skipping registration of ore {}", rl);
            }
        });

        Prospecting.populateDepositBlocks();
    }
}
