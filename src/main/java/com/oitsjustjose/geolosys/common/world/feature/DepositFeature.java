package com.oitsjustjose.geolosys.common.world.feature;

import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.capability.Chunk.IChunkGennedCapability;
import com.oitsjustjose.geolosys.common.world.capability.Deposit.DepositCapability;
import com.oitsjustjose.geolosys.common.world.capability.Deposit.IDepositCapability;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class DepositFeature extends Feature<NoFeatureConfig> {
    public DepositFeature(Codec<NoFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos,
                            NoFeatureConfig config) {
        if (generator instanceof FlatChunkGenerator) {
            return false;
        }

        IDepositCapability depCap = reader.getWorld().getCapability(GeolosysAPI.GEOLOSYS_WORLD_CAPABILITY)
                .orElseThrow(() -> new RuntimeException("Geolosys Pluton Capability Is Null.."));
        IChunkGennedCapability cgCap = reader.getWorld().getCapability(GeolosysAPI.GEOLOSYS_CHUNK_GEN_CAPABILITY)
                .orElseThrow(() -> new RuntimeException("Geolosys Chunk Gen Capability Is Null.."));

        boolean placedPluton = false;
        boolean placedPending = placePendingBlocks(reader, depCap, cgCap, pos);

        if (reader.getRandom().nextDouble() > CommonConfig.CHUNK_SKIP_CHANCE.get()) {
            IDeposit pluton = GeolosysAPI.plutonRegistry.pick(reader, pos, reader.getRandom());
            if (pluton != null) {
                boolean anyGenerated = pluton.generate(reader, pos, depCap, cgCap) > 0;
                if (anyGenerated) {
                    placedPluton = true;
                    pluton.afterGen(reader, pos, depCap, cgCap);
                }
            }
        }

        // Let our tracker know that we did in fact traverse this chunk
        cgCap.setChunkGenerated(new ChunkPos(pos));
        return placedPending || placedPluton;
    }

    private boolean placePendingBlocks(ISeedReader reader, IDepositCapability depCap, IChunkGennedCapability cgCap, BlockPos origin) {
        ChunkPos cp = new ChunkPos(origin);
        ConcurrentLinkedQueue<DepositCapability.PendingBlock> q = depCap.getPendingBlocks(cp);
        if (cgCap.hasChunkGenerated(cp) && q.size() > 0) {
            Geolosys.getInstance().LOGGER.info(
                    "Chunk [{}, {}] has already generated but we're trying to place pending blocks anyways", cp.x,
                    cp.z);
        }
        q.stream().forEach(x -> FeatureUtils.enqueueBlockPlacement(reader, cp, x.getPos(), x.getState(), depCap, cgCap));
        depCap.removePendingBlocksForChunk(cp);
        Geolosys.getInstance().LOGGER.info("Still {} pending blocks", depCap.getPendingBlockCount());
        return q.size() > 0;
    }
}
