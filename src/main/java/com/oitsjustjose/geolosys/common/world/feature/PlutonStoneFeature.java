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
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PlutonStoneFeature extends Feature<NoFeatureConfig> {
    public PlutonStoneFeature(Codec<NoFeatureConfig> p_i231976_1_) {
        super(p_i231976_1_);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos,
            NoFeatureConfig config) {
        if (generator instanceof FlatChunkGenerator) {
            return false;
        }

        IGeolosysCapability plutonCapability = reader.getWorld().getCapability(GeolosysAPI.GEOLOSYS_WORLD_CAPABILITY)
                .orElse(null);
        if (plutonCapability == null) {
            Geolosys.getInstance().LOGGER.info("NULL PLUTON CAPABILITY!!!");
            return false;
        }

        ChunkPosDim chunkPosDim = new ChunkPosDim(pos, Objects.requireNonNull(Utils.dimensionToString(reader)));
        if (plutonCapability.hasStonePlutonGenerated(chunkPosDim)) {
            return false;
        }

        IDeposit pluton = GeolosysAPI.plutonRegistry.pickStone();
        if (pluton == null) {
            return false;
        }

        for (String s : pluton.getDimensionFilter()) {
            boolean a = pluton.isDimensionFilterBlacklist();
            boolean b = Utils.dimensionToString(reader).equals(new ResourceLocation(s).toString());

            /*
             * If dim blacklist and the current dim is in the BL, cancel OR if dim whitelist
             * and the current dim is NOT in the BL, cancel
             */
            if ((a && b) || (!a && !b)) {
                return false;
            }
        }

        if (FeatureUtils.generateDense(reader, pos, rand, pluton, plutonCapability)) {
            plutonCapability
                    .setStonePlutonGenerated(new ChunkPosDim(pos.getX(), pos.getZ(), Utils.dimensionToString(reader)));
            return true;
        }

        return false;
    }
}