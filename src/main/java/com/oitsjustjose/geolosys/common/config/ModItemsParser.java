/* This class is meant to handle reloading join-time data while it's "hot" */

package com.oitsjustjose.geolosys.common.config;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.world.SampleUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ModItemsParser {
    private void load() {
        loadSamplePlacementBlacklist();
    }

    private void loadSamplePlacementBlacklist() {
        CommonConfig.SAMPLE_PLACEMENT_BLACKLIST.get().forEach(s -> {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
            if (block != null) {
                SampleUtils.addSamplePlacementBlacklist(block);
                Geolosys.getInstance().LOGGER.info("Successfully added block {} to samplePlacementBlacklist",
                        block.getRegistryName());
            } else {
                Geolosys.getInstance().LOGGER
                        .warn("The item {} in the samplePlacementBlacklist config option was not valid", s);
            }
        });
    }

    @SubscribeEvent
    public void onServerStart(final ServerAboutToStartEvent evt) {
        load();
    }

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new PreparableReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier p_10638_, ResourceManager p_10639_, ProfilerFiller p_10640_, ProfilerFiller p_10641_, Executor p_10642_, Executor p_10643_) {
                return CompletableFuture.runAsync(() -> {
                    load();
                }, p_10642_).thenCompose(p_10638_::wait);
            }
        });
    }
}
