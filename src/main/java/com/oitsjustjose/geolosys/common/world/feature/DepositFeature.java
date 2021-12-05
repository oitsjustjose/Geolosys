package com.oitsjustjose.geolosys.common.world.feature;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.capability.DepositCapability;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map.Entry;

public class DepositFeature extends Feature<NoneFeatureConfiguration> {
    public DepositFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        WorldGenLevel level = f.level();
        BlockPos pos = f.origin();

        IDepositCapability cap = level.getLevel().getCapability(DepositCapability.CAPABILITY).orElse(null);
        if (cap == null) {
            Geolosys.getInstance().LOGGER.error("NULL PLUTON CAPABILITY!!!");
            return false;
        }

        boolean placedPending = placePendingBlocks(level, cap, pos);
        ChunkPos chunkPos = new ChunkPos(pos);
        if (cap.hasPlutonGenerated(chunkPos)) {
            return false;
        }

        IDeposit pluton = GeolosysAPI.plutonRegistry.pick(level, pos);
        if (pluton == null) { // Could be no pluton for the dimension
            return false;
        }

        if (level.getRandom().nextInt(CommonConfig.CHUNK_SKIP_CHANCE.get()) > pluton.getGenWt()) {
            return false;
        }

        boolean anyGenerated = pluton.generate(level, pos, cap) > 0;

        if (anyGenerated) {
            pluton.afterGen(level, pos, cap);
            cap.setPlutonGenerated(chunkPos);
            return true;
        }

        return placedPending;
    }

    private boolean placePendingBlocks(WorldGenLevel level, IDepositCapability cap, BlockPos pos) {
        boolean placedAny = false;
        for (Entry<BlockPos, BlockState> e : cap.getPendingBlocks().entrySet()) {
            if (FeatureUtils.isInChunk(new ChunkPos(pos), e.getKey())) {
                if (level.setBlock(e.getKey(), e.getValue(), 2 | 16)) {
                    placedAny = true;
                    cap.getPendingBlocks().remove(e.getKey());
                    if (CommonConfig.ADVANCED_DEBUG_WORLD_GEN.get()) {
                        Geolosys.getInstance().LOGGER.debug("Generated pending block "
                                + e.getValue().getBlock().getRegistryName().toString() + " at " + e.getKey());
                    }
                } else {
                    if (CommonConfig.DEBUG_WORLD_GEN.get()) {
                        Geolosys.getInstance().LOGGER.error("FAILED to generate pending block "
                                + e.getValue().getBlock().getRegistryName().toString() + " at " + e.getKey());
                    }
                }
            }
        }

        return placedAny;
    }
}
