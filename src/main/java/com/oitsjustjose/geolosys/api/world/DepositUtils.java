package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public class DepositUtils {
    private static Random random = new Random();
    private static HashSet<BlockState> defaultMatchersCached = null;

    /**
     * picks a choice out of a mapping between blockstate to weight passing -1.0F as
     * totl will result in a total being calculated.
     *
     * @param map
     * @param totl
     * @return null if no block should be used or placed, T instanceof BlockState if
     *         actual block should be placed.
     */
    @Nullable
    public static BlockState pick(HashMap<BlockState, Float> map, float totl) {
        if (totl == 1.0F) {
            totl = 0;
            for (Entry<BlockState, Float> e : map.entrySet()) {
                totl += e.getValue();
            }
        }

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

    public static boolean canPlaceInBiome(Biome targetBiome, @Nullable List<Biome> biomes,
            @Nullable List<BiomeDictionary.Type> biomeTypes, boolean isBiomeFilterBl) {
        boolean matchForBiome = false;
        boolean matchForBiomeType = false;

        if (biomes != null) {
            matchForBiome = biomes.stream()
                    .anyMatch((b) -> Objects.equals(b.getRegistryName(), targetBiome.getRegistryName()));
        }

        if (biomeTypes != null) {
            try {
                Set<BiomeDictionary.Type> types = BiomeDictionary
                        .getTypes(ForgeRegistries.BIOMES.getResourceKey(targetBiome).orElse(null));
                matchForBiomeType = types.stream().anyMatch(a -> {
                    // TODO: Verify that type comparison works here.
                    return biomeTypes.contains(a);
                });
            } catch (NullPointerException e) {
                Geolosys.getInstance().LOGGER.error(e);
            }
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
}
