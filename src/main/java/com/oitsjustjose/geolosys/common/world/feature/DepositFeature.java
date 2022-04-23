package com.oitsjustjose.geolosys.common.world.feature;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.capability.deposit.DepositCapability;
import com.oitsjustjose.geolosys.capability.deposit.DepositCapability.PendingBlock;
import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DepositFeature extends Feature<NoneFeatureConfiguration> {
    public DepositFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
    }

    public final DepositFeature withRegistryName(String modID, String name) {
        this.setRegistryName(new ResourceLocation(modID, name));
        return this;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        WorldGenLevel level = f.level();
        BlockPos pos = f.origin();

        IDepositCapability depCap = level.getLevel().getCapability(DepositCapability.CAPABILITY)
                .orElseThrow(() -> new RuntimeException("Geolosys Pluton Capability Is Null.."));

        boolean placedPluton = false;
        boolean placedPending = placePendingBlocks(level, depCap, pos);

        if (level.getRandom().nextDouble() > CommonConfig.CHUNK_SKIP_CHANCE.get()) {
            for (int p = 0; p < CommonConfig.NUMBER_PLUTONS_PER_CHUNK.get(); p++) {
                IDeposit pluton = GeolosysAPI.plutonRegistry.pick(level, pos);
                if (pluton == null) {
                    continue;
                }

                boolean anyGenerated = pluton.generate(level, pos, depCap) > 0;
                if (anyGenerated) {
                    placedPluton = true;
                    pluton.afterGen(level, pos, depCap);
                }
            }
        }

        return placedPluton || placedPending;
    }

    private boolean placePendingBlocks(WorldGenLevel level, IDepositCapability cap, BlockPos origin) {
        ChunkPos cp = new ChunkPos(origin);
        ConcurrentLinkedQueue<PendingBlock> q = cap.getPendingBlocks(cp);
        q.stream().forEach(x -> forceSetBlock(level, x.getPos(), x.getState()));
        cap.removePendingBlocksForChunk(cp);
        return q.size() > 0;
    }

    private void forceSetBlock(WorldGenLevel level, BlockPos pos, BlockState state) {
        ChunkAccess chunkaccess = level.getChunk(pos);
        BlockState blockstate = chunkaccess.setBlockState(pos, state, false);
        if (blockstate != null) {
            level.getLevel().onBlockStateChange(pos, blockstate, state);
        }
    }
}
