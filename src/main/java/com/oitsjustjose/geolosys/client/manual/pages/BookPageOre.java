package com.oitsjustjose.geolosys.client.manual.pages;

import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

/**
 * @author oitsjustjose,
 * @author Mangoose / https://github.com/the-realest-stu/ Code inspired directly from:
 * https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

public class BookPageOre extends BookPage
{
    private IDeposit ore;
    private long lastUpdate;
    private ItemStack displayStack;

    public BookPageOre(IDeposit ore)
    {
        super(ore.getFriendlyName());
        this.ore = ore;
        this.lastUpdate = System.currentTimeMillis();
        BlockState tmp = ore.getOre();
        this.displayStack = new ItemStack(tmp.getBlock().asItem());
    }

    public ItemStack getDisplayStack()
    {
        if (this.isMultiOre())
        {
            if (System.currentTimeMillis() - this.lastUpdate >= 1000)
            {
                BlockState tmp = ore.getOre();
                this.displayStack = new ItemStack(tmp.getBlock().asItem());
                this.lastUpdate = System.currentTimeMillis();
            }
        }
        return this.displayStack;
    }

    public int getMinY()
    {
        return this.ore.getYMin();
    }

    public int getMaxY()
    {
        return this.ore.getYMax();
    }

    public int getSize()
    {
        return this.ore.getSize();
    }

    public int getChance()
    {
        return this.ore.getChance();
    }

    public boolean isMultiOre()
    {
        return this.ore instanceof DepositMultiOre;
    }

    public boolean isBiomeRestricted()
    {
        return this.ore instanceof DepositBiomeRestricted || this.ore instanceof DepositMultiOreBiomeRestricted;
    }

    public String getFriendlyName()
    {
        if (this.ore instanceof DepositMultiOre)
        {
            return this.ore.getFriendlyName();
        }
        return this.ore.getFriendlyName();
    }

    public String getBiomes()
    {
        if (this.ore instanceof DepositBiomeRestricted)
        {
            DepositBiomeRestricted biomeRestricted = (DepositBiomeRestricted) this.ore;

            StringBuilder sb = new StringBuilder();
            for (Biome b : biomeRestricted.getBiomeList())
            {
                sb.append(", ");
                sb.append(b.getDisplayName().getFormattedText());
            }
            for (BiomeDictionary.Type type : biomeRestricted.getBiomeTypes())
            {
                sb.append(", ");
                sb.append(type.getName().toLowerCase());
            }

            String sbRet = sb.toString().substring(2);
            int commaCount = sbRet.split(",").length;
            if (commaCount == 1)
            {
                return sbRet.replace(",", " &");
            }
            else if (commaCount > 1)
            {
                return sbRet.substring(0, sbRet.lastIndexOf(",")) + " &"
                        + sbRet.substring(sbRet.lastIndexOf(",") + 1);
            }
            // commaCount == 0
            return sbRet;
        }
        if (this.ore instanceof DepositMultiOreBiomeRestricted)
        {
            DepositMultiOreBiomeRestricted biomeRestricted = (DepositMultiOreBiomeRestricted) this.ore;

            StringBuilder sb = new StringBuilder();
            for (Biome b : biomeRestricted.getBiomeList())
            {
                sb.append(", ");
                sb.append(b.getDisplayName().getFormattedText());
            }
            for (BiomeDictionary.Type type : biomeRestricted.getBiomeTypes())
            {
                sb.append(", ");
                sb.append(type.getName().toLowerCase());
            }

            String sbRet = sb.toString().substring(2);
            int commaCount = sbRet.split(",").length;
            if (commaCount == 1)
            {
                return sbRet.replace(",", " &");
            }
            else if (commaCount > 1)
            {
                return sbRet.substring(0, sbRet.lastIndexOf(",")) + " &"
                        + sbRet.substring(sbRet.lastIndexOf(",") + 1);
            }
            // commaCount == 0
            return sbRet;
        }
        return null;
    }

    public int getHarvestLevel()
    {
        if (this.isMultiOre())
        {
            DepositMultiOre multiOre = (DepositMultiOre) this.ore;
            int highest = 0;
            for (BlockState state : multiOre.getOres())
            {
                if (state.getHarvestLevel() > highest)
                {
                    highest = state.getBlock().getHarvestLevel(state);
                }
            }
            return highest;
        }
        return this.ore.getOre().getBlock().getHarvestLevel(this.ore.getOre());
    }
}
