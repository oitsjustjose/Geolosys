package com.oitsjustjose.geolosys.compat;

import java.util.Random;

import exterminatorjeff.undergroundbiomes.api.event.UBForceReProcessEvent;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;

public class UBCompat
{
    public static void forceReprocess(IChunkGenerator chunkGenerator, World world, Random random, int chunkX,
            int chunkZ)
    {
        MinecraftForge.EVENT_BUS.post(new UBForceReProcessEvent(chunkGenerator, world, random, chunkX, chunkZ, true));
    }
}