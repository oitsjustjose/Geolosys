package com.oitsjustjose.geolosys.api.world;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

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

    public static boolean canPlaceInBiome(Biome biome, @Nullable List<Biome> biomes,
            @Nullable List<BiomeDictionary.Type> biomeTypes, boolean isBiomeFilterBl) {
        boolean matchForBiome = false;
        boolean matchForBiomeType = false;

        if (biomes != null) {
            matchForBiome = biomes.stream().anyMatch((b) -> {
                return b == biome;
            });
        }

        if (biomeTypes != null) {
            for (BiomeDictionary.Type type : biomeTypes) {
                RegistryKey<Biome> regKey = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
                Set<BiomeDictionary.Type> dictTypes = BiomeDictionary.getTypes(regKey);
                matchForBiomeType = dictTypes.stream().anyMatch((dictType) -> {
                    return type.getName().equalsIgnoreCase(dictType.getName());
                });
                if (matchForBiomeType) {
                    break;
                }
            }
        }

        return ((matchForBiome || matchForBiomeType) && !isBiomeFilterBl)
                || (!(matchForBiome || matchForBiomeType) && isBiomeFilterBl);
    }

    @SuppressWarnings("unchecked")
    public static HashSet<BlockState> getDefaultMatchers() {
        // If the cached data isn't there yet, load it.
        if (defaultMatchersCached == null) {
            defaultMatchersCached = new HashSet<BlockState>();
            // GeolosysAPI.plutonRegistry.getStones().forEach(x ->
            // defaultMatchersCached.add(x.getOre()));

            CommonConfig.DEFAULT_REPLACEMENT_MATS.get().forEach(s -> {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
                if (block == null || !addDefaultMatcher(block)) {
                    Geolosys.getInstance().LOGGER.warn("{} is not a valid block. Please verify.", s);
                }
            });
        }

        return (HashSet<BlockState>) defaultMatchersCached.clone();
    }

    @SuppressWarnings("deprecation")
    public static boolean addDefaultMatcher(Block block) {
        BlockState defaultState = block.getDefaultState();
        if (!defaultState.isAir()) {
            defaultMatchersCached.add(defaultState);
            return true;
        }
        return false;
    }

    /**
     * Takes a given BlockPos and world, and clamps the Y to be below the highest
     * block in that x/z.
     * 
     * @param iworld
     * @param pos
     * @return the provided pos if iworld is WorldGenRegion or somehow no
     *         improvement is found..
     */
    public static int getMaxTerrainHeight(IWorld iworld, int x, int z) {
        if (!(iworld instanceof WorldGenRegion)) {
            return Integer.MAX_VALUE;
        }

        WorldGenRegion world = (WorldGenRegion) iworld;
        BlockPos searchPos = new BlockPos(x, 0, z);

        while (searchPos.getY() < world.getHeight()) {
            BlockState s = world.getBlockState(searchPos);
            if (!s.isAir() && s.isSolid() && world.canBlockSeeSky(searchPos)) {
                return searchPos.down(3).getY();
            }
            searchPos = searchPos.up();
        }

        return Integer.MAX_VALUE;
    }
}
