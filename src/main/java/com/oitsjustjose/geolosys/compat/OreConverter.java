package com.oitsjustjose.geolosys.compat;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.config.ModConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class OreConverter
{
    @SubscribeEvent
    public void registerEvent(BlockEvent.HarvestDropsEvent event)
    {
        if (!ModConfig.featureControl.retroReplace)
        {
            return;
        }
        String ore = getOreDictOre(new ItemStack(Item.getItemFromBlock(event.getState().getBlock()), 1, event.getState().getBlock().getMetaFromState(event.getState())));
        if (ore != null)
        {
            if (ModMaterials.clusterConverter.containsKey(ore))
            {
                event.getDrops().clear();
                event.getDrops().add(ModMaterials.clusterConverter.get(ore));
            }
        }
    }

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
                    if (getOreDictOre(world.getBlockState(new BlockPos(x + xMod, y, z + zMod))) != null)
                    {
                        world.setBlockState(new BlockPos(x + xMod, y, z + zMod), ModMaterials.blockConverter.get(getOreDictOre(world.getBlockState(new BlockPos(x + xMod, y, z + zMod)))));
                    }
                }
            }
        }
        GeolosysAPI.markChunkRegenned(new ChunkPos(event.getNewChunkX(), event.getNewChunkZ()), world.provider.getDimension());
        GeolosysAPI.writeToFile();
    }


    private String getOreDictOre(ItemStack stack)
    {
        for (String ore : ModMaterials.clusterConverter.keySet())
        {
            for (ItemStack oreStack : OreDictionary.getOres(ore))
            {
                if (oreStack.getItem() == stack.getItem() && oreStack.getMetadata() == stack.getMetadata())
                {
                    return ore;
                }
            }
        }
        return null;
    }

    private String getOreDictOre(IBlockState state)
    {
        for (String ore : ModMaterials.clusterConverter.keySet())
        {
            for (ItemStack oreStack : OreDictionary.getOres(ore))
            {
                if (oreStack.getItem() == Item.getItemFromBlock(state.getBlock()) && oreStack.getMetadata() == state.getBlock().getMetaFromState(state))
                {
                    return ore;
                }
            }
        }
        return null;
    }
}
