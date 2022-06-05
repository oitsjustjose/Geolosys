package com.oitsjustjose.geolosys.common.world.feature;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.capability.deposit.DepositCapability;
import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.capability.world.ChunkGennedCapability;
import com.oitsjustjose.geolosys.capability.world.IChunkGennedCapability;

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
import net.minecraftforge.registries.ObjectHolder;

public class RemoveVeinsFeature extends Feature<NoneFeatureConfiguration> {

    private final ArrayList<Block> UNACCEPTABLE = Lists.newArrayList(
            Blocks.RAW_IRON_BLOCK,
            Blocks.RAW_COPPER_BLOCK,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.COPPER_ORE
    );

    // TODO get better way to get instances of the blocks
    @ObjectHolder("geolosys:malachite_ore")
    public static final Block geolosysMalachiteOreBlock = null;

    @ObjectHolder("geolosys:deepslate_hematite_ore")
    public static final Block geolosysDeepslateHematiteOreBlock = null;

    private final HashMap<Block, Block> oreReplacementMap;
    public RemoveVeinsFeature(Codec<NoneFeatureConfiguration> p_i231976_1_) {
        super(p_i231976_1_);
        oreReplacementMap = initOreReplacementMap();
    }

    private HashMap<Block, Block> initOreReplacementMap() {
        HashMap<Block, Block> map = new HashMap<>(){{
            put(Blocks.COPPER_ORE, geolosysMalachiteOreBlock);
            put(Blocks.RAW_COPPER_BLOCK, geolosysMalachiteOreBlock);
            put(Blocks.DEEPSLATE_IRON_ORE, geolosysDeepslateHematiteOreBlock);
            put(Blocks.RAW_IRON_BLOCK, geolosysDeepslateHematiteOreBlock);
        }};

        if (CommonConfig.REMOVE_VEIN_ORES.get()) {
            map.put(Blocks.COPPER_ORE, Blocks.STONE);
            map.put(Blocks.RAW_COPPER_BLOCK, Blocks.STONE);
            map.put(Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE);
            map.put(Blocks.RAW_IRON_BLOCK, Blocks.DEEPSLATE);
        }
        return map;
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
        IDepositCapability deposits = level.getLevel().getCapability(DepositCapability.CAPABILITY)
                .orElseThrow(() -> new RuntimeException(
                        "Geolosys detected a null Pluton capability somehow. Are any invasive world gen mods active?"));
        IChunkGennedCapability chunks = level.getLevel().getCapability(ChunkGennedCapability.CAPABILITY)
                .orElseThrow(() -> new RuntimeException(
                        "Geolosys detected a null Pluton capability somehow. Are any invasive world gen mods active?"));

        for (int x = cp.getMinBlockX(); x <= cp.getMaxBlockX(); x++) {
            for (int z = cp.getMinBlockZ(); z <= cp.getMaxBlockZ(); z++) {
                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
                    BlockPos p = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(p);
                    if (UNACCEPTABLE.contains(state.getBlock())) {
                        BlockState properReplacement = oreReplacementMap.get(state.getBlock()).defaultBlockState();
                        FeatureUtils.enqueueBlockPlacement(level, cp, p, properReplacement, deposits, chunks);
                    }
                }
            }
        }

        return true;
    }
}
