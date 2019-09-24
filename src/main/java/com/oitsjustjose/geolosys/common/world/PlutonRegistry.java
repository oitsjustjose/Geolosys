package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.api.world.DepositStone;
import com.oitsjustjose.geolosys.api.world.IOre;
import com.oitsjustjose.geolosys.common.world.feature.PlutonFeature;
import com.oitsjustjose.geolosys.common.world.feature.PlutonFeatureConfig;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class PlutonRegistry
{
    private ArrayList<IOre> ores;
    private ArrayList<DepositStone> stones;
    private static PlutonRegistry instance;

    private PlutonRegistry()
    {
        this.ores = new ArrayList<>();
        this.stones = new ArrayList<>();
    }

    public boolean addOrePluton(IOre ore)
    {
        if (ore instanceof DepositStone)
        {
            return addStonePluton((DepositStone) ore);
        }
        return this.ores.add(ore);
    }

    public boolean addStonePluton(DepositStone ore)
    {
        return this.stones.add(ore);
    }

    public static PlutonRegistry getInstance()
    {
        if (instance == null)
        {
            instance = new PlutonRegistry();
        }
        return instance;
    }

    public void register()
    {
        for (IOre ore : this.ores)
        {
            for (Biome biome : ForgeRegistries.BIOMES.getValues())
            {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                        Biome.createDecoratedFeature(new PlutonFeature(PlutonFeatureConfig::deserialize),
                                new PlutonFeatureConfig(ore), Placement.COUNT_RANGE,
                                new CountRangeConfig(1, 0, 0, 16)));
            }
        }
        for (DepositStone stone : this.stones)
        {
            for (Biome biome : ForgeRegistries.BIOMES.getValues())
            {
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                        Biome.createDecoratedFeature(new PlutonFeature(PlutonFeatureConfig::deserialize),
                                new PlutonFeatureConfig(stone), Placement.COUNT_RANGE,
                                new CountRangeConfig(1, 0, 0, 16)));
            }
        }
    }
}