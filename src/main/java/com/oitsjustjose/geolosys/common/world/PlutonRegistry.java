package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.DepositStone;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.world.feature.PlutonOreFeature;
import com.oitsjustjose.geolosys.common.world.feature.PlutonStoneFeature;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Random;

public class PlutonRegistry
{
    private ArrayList<IDeposit> ores;
    private ArrayList<DepositStone> stones;
    private ArrayList<IDeposit> oreWeightList;

    private static PlutonRegistry instance;

    private PlutonRegistry()
    {
        this.ores = new ArrayList<>();
        this.stones = new ArrayList<>();
        this.oreWeightList = new ArrayList<>();
    }

    public boolean addOrePluton(IDeposit ore)
    {
        if (ore instanceof DepositStone)
        {
            return addStonePluton((DepositStone) ore);
        }

        for (int i = 0; i < ore.getChance(); i++)
        {
            oreWeightList.add(ore);
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

    public IDeposit pickPluton()
    {
        if (this.oreWeightList.size() > 0)
        {
            Random random = new Random();
            int pick = random.nextInt(this.oreWeightList.size());
            return this.oreWeightList.get(pick);
        }
        Geolosys.getInstance().LOGGER
                .error("There aren't any ores in the oreWeightList - something will likely break soon");
        return null;
    }

    public void register()
    {
        for (Biome biome : ForgeRegistries.BIOMES.getValues())
        {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                    Biome.createDecoratedFeature(new PlutonOreFeature(NoFeatureConfig::deserialize), new NoFeatureConfig(),
                            Placement.COUNT_RANGE, new CountRangeConfig(1, 0, 0, 1)));
        }
        for (Biome biome : ForgeRegistries.BIOMES.getValues())
        {
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                    Biome.createDecoratedFeature(new PlutonStoneFeature(NoFeatureConfig::deserialize),
                            new NoFeatureConfig(), Placement.COUNT_RANGE, new CountRangeConfig(1, 0, 0, 1)));
        }
    }
}