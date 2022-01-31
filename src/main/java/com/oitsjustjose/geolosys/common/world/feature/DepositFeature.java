package com.oitsjustjose.geolosys.common.world.feature;

import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.capability.DepositCapability;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
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

        if (level.getRandom().nextDouble() < CommonConfig.CHUNK_SKIP_CHANCE.get()) {
            return false;
        }

        IDepositCapability cap = level.getLevel().getCapability(DepositCapability.CAPABILITY).orElse(null);
        if (cap == null) {
            Geolosys.getInstance().LOGGER.error("NULL PLUTON CAPABILITY!!!");
            return false;
        }

        boolean placedPending = placePendingBlocks(level, cap, pos);
        boolean placedPluton = false;

        for (int p = 0; p < CommonConfig.NUMBER_PLUTONS_PER_CHUNK.get(); p++) {
            IDeposit pluton = GeolosysAPI.plutonRegistry.pick(level, pos);
            if (pluton == null) {
                continue;
            }

            boolean anyGenerated = pluton.generate(level, pos, cap) > 0;
            if (anyGenerated) {
                placedPluton = true;
                pluton.afterGen(level, pos, cap);
            }
        }

        return placedPluton || placedPending;
    }

    private boolean placePendingBlocks(WorldGenLevel level, IDepositCapability cap, BlockPos origin) {
        ChunkPos cp = new ChunkPos(origin);
        /* Filter out only pending blocks for *this* chunk */
        Map<BlockPos, BlockState> forThisChunk = cap.getPendingBlocks(cp);
        forThisChunk.forEach((pos, state) -> level.setBlock(pos, state, 2 | 16));
        forThisChunk.forEach((pos, state) -> cap.removePendingBlock(pos, state));
        return forThisChunk.size() > 0;
    }
}
