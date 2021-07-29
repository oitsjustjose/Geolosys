/* This class is meant to handle reloading join-time data while it's "hot" */

package com.oitsjustjose.geolosys.common.config;

import javax.annotation.Nonnull;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.world.SampleUtils;

import net.minecraft.block.Block;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemsParser {
    private void load() {
        loadSamplePlacementBlacklist();
        loadProPickExtras();
    }

    private void loadSamplePlacementBlacklist() {
        CommonConfig.SAMPLE_PLACEMENT_BLACKLIST.get().forEach(s -> {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
            if (block != null) {
                SampleUtils.addSamplePlacementBlacklist(block);
                Geolosys.getInstance().LOGGER.info(
                        "Successfully added block {} to samplePlacementBlacklist",
                        block.getRegistryName());
            } else {
                Geolosys.getInstance().LOGGER.warn(
                        "The item {} in the samplePlacementBlacklist config option was not valid",
                        s);
            }
        });
    }

    private void loadProPickExtras() {
        CommonConfig.PRO_PICK_EXTRAS.get().forEach(s -> {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
            if (block != null && block.getDefaultState().isAir()) {
                GeolosysAPI.proPickExtras.add(block.getDefaultState());
                Geolosys.getInstance().LOGGER.info("Successfully added block {} to proPickExtras",
                        block.getRegistryName());
            } else {
                Geolosys.getInstance().LOGGER
                        .warn("The item {} in the proPickExtras config option was not valid", s);
            }
        });
    }

    @SubscribeEvent
    public void onServerStart(final FMLServerAboutToStartEvent evt) {
        load();
    }

    @SubscribeEvent
    public void registerSlashReloadLogic(AddReloadListenerEvent evt) {
        evt.addListener(new ReloadListener<Void>() {
            @Override
            protected void apply(@Nonnull Void objectIn, @Nonnull IResourceManager resourceMgr,
                    @Nonnull IProfiler profilerIn) {
                load();
            }

            @Override
            @Nonnull // Ironic considering I'm returning null anyways.. ok tho
            protected Void prepare(@Nonnull IResourceManager resourceMgr,
                    @Nonnull IProfiler profilerIn) {
                return null;
            }
        });
    }
}
