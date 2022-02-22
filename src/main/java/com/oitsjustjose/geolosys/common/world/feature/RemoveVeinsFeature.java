package com.oitsjustjose.geolosys.common.world.feature;

import java.util.ArrayList;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.capability.DepositCapability;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class RemoveVeinsFeature extends Feature<NoneFeatureConfiguration> {

    private final ArrayList<Block> UNACCEPTABLE = Lists.newArrayList(Blocks.RAW_IRON_BLOCK, Blocks.RAW_COPPER_BLOCK,
            Blocks.DEEPSLATE_IRON_ORE, Blocks.COPPER_ORE);

    public RemoveVeinsFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
    }

    public final RemoveVeinsFeature withRegistryName(String modID, String name) {
        this.setRegistryName(new ResourceLocation(modID, name));
        return this;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> f) {
        if (f.chunkGenerator() instanceof FlatLevelSource) {
            return false;
        }

        if (!CommonConfig.REMOVE_VANILLA_ORES.get()) {
            return true;
        }

        WorldGenLevel level = f.level();
        ChunkPos cp = new ChunkPos(f.origin());
        IDepositCapability cap = level.getLevel().getCapability(DepositCapability.CAPABILITY)
                .orElseThrow(() -> new RuntimeException("Geolosys detected a null Pluton capability somehow. Are any invasive world gen mods active?"));

        for (int x = cp.getMinBlockX(); x <= cp.getMaxBlockX(); x++) {
            for (int z = cp.getMinBlockZ(); z <= cp.getMaxBlockZ(); z++) {
                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
                    BlockPos p = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(p);
                    if (UNACCEPTABLE.contains(state.getBlock())) {
                        FeatureUtils.tryPlaceBlock(level, cp, p, Blocks.TUFF.defaultBlockState(), cap);
                    }
                }
            }
        }

        return true;
    }
}
