package com.oitsjustjose.geolosys.common.compat;

import java.util.Random;

import com.oitsjustjose.geolosys.common.compat.modules.drops.OsmiumDrop;
import com.oitsjustjose.geolosys.common.compat.modules.drops.SulfurDrop;
import com.oitsjustjose.geolosys.common.compat.modules.drops.YelloriumDrop;

import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CompatLoader {
    public static void init() {
        MinecraftForge.EVENT_BUS.register(new OsmiumDrop());
        MinecraftForge.EVENT_BUS.register(new SulfurDrop());
        MinecraftForge.EVENT_BUS.register(new YelloriumDrop());
    }

    public static void injectDrop(World world, BlockPos pos, Random rand, ItemStack stack, boolean replace) {
        double x = (double) pos.getX();
        double y = (double) pos.getY();
        double z = (double) pos.getZ();
        double dX = (double) (rand.nextFloat() * 0.7F) + (double) 0.15F;
        double dZ = (double) (rand.nextFloat() * 0.7F) + (double) 0.15F;

        if (replace) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 16);
        }

        ItemEntity ent = new ItemEntity(world, x + dX, y, z + dZ, stack);
        ent.setDefaultPickupDelay();
        world.addEntity(ent);
    }
}