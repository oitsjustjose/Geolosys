package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class DepositUtils {
    private static HashSet<BlockState> defaultMatchersCached = null;

    /**
     * picks a choice out of a mapping between blockstate to weight passing -1.0F as
     * totl will result in a total being calculated.
     *
     * @param map  the map between a blockstate and its chance
     * @param totl the total of all chances
     * @return null if no block should be used or placed, T instanceof BlockState if
     * actual block should be placed.
     */
    @Nullable
    public static BlockState pick(HashMap<BlockState, Float> map, float totl, RandomSource random) {
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
