package com.oitsjustjose.geolosys.common.utils;

import com.oitsjustjose.geolosys.common.blocks.Types.Ores;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GeolosysGroup extends ItemGroup {
    private static GeolosysGroup instance;

    private int counter = -1;
    private long lastTick = 0L;
    private ItemStack display = new ItemStack(Blocks.STONE);

    private GeolosysGroup() {
        super("geolosys.name");
    }

    public static GeolosysGroup getInstance() {
        if (instance == null) {
            instance = new GeolosysGroup();
        }
        return instance;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Blocks.STONE);
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getIcon() {
        // Init the anim only when the first ore is init'd
        if (Ores.COAL.getBlock() != null && counter == -1) {
            counter = 0;
        }

        if (System.currentTimeMillis() - lastTick > 1000L) {
            display = new ItemStack(Ores.values()[counter].getBlock());
            counter = counter == (Ores.values().length - 1) ? 0 : counter + 1;
            lastTick = System.currentTimeMillis();
        }
        return display;
    }
}
