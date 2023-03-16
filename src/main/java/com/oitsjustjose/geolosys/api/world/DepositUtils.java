package com.oitsjustjose.geolosys.api.world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

public class DepositUtils {
    private static Random random = new Random();
    private static HashSet<BlockState> defaultMatchersCached = null;

    /**
     * picks a choice out of a mapping between blockstate to weight passing -1.0F as
     * totl will result in a total being calculated.
     *
     * @param map  the map between a blockstate and its chance
     * @param totl the total of all chances
     * @return null if no block should be used or placed, T instanceof BlockState if
     *         actual block should be placed.
     */
    @Nullable
    public static BlockState pick(HashMap<BlockState, Float> map, float totl) {
        float rng = random.nextFloat();
        for (Entry<BlockState, Float> e : map.entrySet()) {
            float wt = e.getValue();
            if (rng < wt) {
                return e.getKey();
            }
            rng -= wt;
        }

        Geolosys.getInstance().LOGGER.error("Could not reach decision on block to place at Utils#pick");
        return null;
    }

    public static boolean canPlaceInBiome(Holder<Biome> targetBiome, @Nullable List<Biome> biomes,
            @Nullable List<BiomeDictionary.Type> biomeTypes, boolean isBiomeFilterBl) {
        boolean matchForBiome = false;
        boolean matchForBiomeType = false;

        if (biomes != null) {
            matchForBiome = biomes.stream().anyMatch((b) -> targetBiome.is(b.getRegistryName()));
        }

        if (biomeTypes != null) {
            matchForBiomeType = biomeTypes.stream().anyMatch(t -> BiomeDictionary.getBiomes(t).stream()
                    .anyMatch(b -> targetBiome.is(b.location())));
        }

        return ((matchForBiome || matchForBiomeType) && !isBiomeFilterBl)
                || (!matchForBiome && !matchForBiomeType && isBiomeFilterBl);
    }

    @SuppressWarnings("unchecked")
    public static HashSet<BlockState> getDefaultMatchers() {
        // If the cached data isn't there yet, load it.
        if (defaultMatchersCached == null) {
            defaultMatchersCached = new HashSet<>();
            CommonConfig.DEFAULT_REPLACEMENT_MATS.get().forEach(s -> {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
                if (block == null || !addDefaultMatcher(block)) {
                    Geolosys.getInstance().LOGGER.warn("{} is not a valid block. Please verify.", s);
                }
            });
        }

        return (HashSet<BlockState>) defaultMatchersCached.clone();
    }

    public static boolean addDefaultMatcher(Block block) {
        BlockState defaultState = block.defaultBlockState();
        if (!defaultState.isAir()) {
            defaultMatchersCached.add(defaultState);
            return true;
        }
        return false;
    }

    /**
     * Returns true if a and b are within epsilon of each other, where epsilon is the minimum
     * representable value by a 32-bit floating point number.
     */
    public static boolean nearlyEquals(float a, float b) {
        return Math.abs(a - b) <= Float.MIN_VALUE;
    }
}
