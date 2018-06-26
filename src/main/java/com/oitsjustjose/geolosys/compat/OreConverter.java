package com.oitsjustjose.geolosys.compat;

import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.util.HelperFunctions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

public class OreConverter
{
    @SubscribeEvent
    public void registerEvent(PlayerEvent.EnteringChunk event)
    {
        if (!ModConfig.featureControl.retroReplace)
        {
            return;
        }
        if (!(event.getEntity() instanceof EntityPlayer) || event.getEntity() instanceof FakePlayer || event.getEntity().getEntityWorld().isRemote)
        {
            return;
        }
        if (GeolosysAPI.hasChunkRegenned(new ChunkPos(event.getNewChunkX(), event.getNewChunkZ()), event.getEntity().getEntityWorld().provider.getDimension()))
        {
            return;
        }
        int x = event.getNewChunkX() * 16;
        int z = event.getNewChunkZ() * 16;
        World world = event.getEntity().getEntityWorld();
        for (int xMod = 0; xMod < 16; xMod++)
        {
            for (int zMod = 0; zMod < 16; zMod++)
            {
                for (int y = 0; y < world.getHeight(x + xMod, z + zMod); y++)
                {
                    if (GeolosysAPI.oreConverterBlacklist.contains(world.getBlockState(new BlockPos(x + xMod, y, z + zMod))))
                    {
                        continue;
                    }
                    if (getConvertedOre(HelperFunctions.blockStateToStack(world.getBlockState(new BlockPos(x + xMod, y, z + zMod)))) != null)
                    {
                        world.setBlockState(new BlockPos(x + xMod, y, z + zMod), Objects.requireNonNull(getConvertedOre(HelperFunctions.blockStateToStack(world.getBlockState(new BlockPos(x + xMod, y, z + zMod))))));
                    }
                }
            }
        }
        GeolosysAPI.markChunkRegenned(new ChunkPos(event.getNewChunkX(), event.getNewChunkZ()), world.provider.getDimension());
        GeolosysAPI.writeToFile();
    }


    private IBlockState getConvertedOre(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return null;
        }
        for (int id : OreDictionary.getOreIDs(stack))
        {
            if (ModMaterials.blockConverter.containsKey(OreDictionary.getOreName(id)))
            {
                return ModMaterials.blockConverter.get(OreDictionary.getOreName(id));
            }
        }
        return null;
    }
}
