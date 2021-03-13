package com.oitsjustjose.geolosys.common.world.feature;

import java.util.Objects;
import java.util.Random;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.ChunkPosDim;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.capability.IGeolosysCapability;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

public class PlutonStoneFeature extends Feature<NoFeatureConfig> {
    public PlutonStoneFeature(Codec<NoFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos,
            NoFeatureConfig config) {
        IWorld iworld = reader.getWorld();
        if (!(iworld instanceof ServerWorld)) {
            return false;
        }

        ServerWorld world = (ServerWorld) iworld;
        if (world.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator) {
            return false;
        }

        IGeolosysCapability plutonCapability = world.getCapability(GeolosysAPI.GEOLOSYS_WORLD_CAPABILITY).orElse(null);
        if (plutonCapability == null) {
            Geolosys.getInstance().LOGGER.info("NULL PLUTON CAPABILITY!!!");
            return false;
        }

        ChunkPosDim chunkPosDim = new ChunkPosDim(pos, Objects.requireNonNull(Utils.dimensionToString(world)));
        if (plutonCapability.hasStonePlutonGenerated(chunkPosDim)) {
            return false;
        }

        IDeposit pluton = GeolosysAPI.plutonRegistry.pickStone();
        if (pluton == null) {
            return false;
        }

        for (String s : pluton.getDimensionFilter()) {
            boolean a = pluton.isDimensionFilterBlacklist();
            boolean b = Utils.dimensionToString(world).equals(new ResourceLocation(s).toString());

            /*
             * If dim blacklist and the current dim is in the BL, cancel OR if dim whitelist
             * and the current dim is NOT in the BL, cancel
             */
            if ((a && b) || (!a && !b)) {
                return false;
            }
        }

        if (func_207803_a(reader, rand, pos, pluton, plutonCapability)) {
            plutonCapability
                    .setStonePlutonGenerated(new ChunkPosDim(pos.getX(), pos.getZ(), Utils.dimensionToString(world)));
            return true;
        }

        return false;
    }

    protected boolean func_207803_a(IWorld world, Random rand, BlockPos pos, IDeposit pluton,
            IGeolosysCapability plutonCapability) {
        if (FeatureUtils.generateDense(world, pos, rand, pluton, plutonCapability)) {
            return true;
        }
        return false;
    }

}