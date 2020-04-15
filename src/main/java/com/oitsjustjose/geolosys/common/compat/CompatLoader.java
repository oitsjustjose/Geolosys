package com.oitsjustjose.geolosys.common.compat;

import javax.annotation.Nonnull;

import com.oitsjustjose.geolosys.common.compat.modules.BigReactors;
import com.oitsjustjose.geolosys.common.compat.modules.Mekanism;
import com.oitsjustjose.geolosys.common.compat.modules.Sulfur;
import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CompatLoader
{
    @SubscribeEvent
    public static void registerModifierSerializers(
            @Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
    {
        event.getRegistry()
                .register(new Mekanism.Serializer().setRegistryName(new ResourceLocation(Constants.MODID, "mekanism")));
        event.getRegistry().register(
                new BigReactors.Serializer().setRegistryName(new ResourceLocation(Constants.MODID, "big_reactors")));
        event.getRegistry()
                .register(new Sulfur.Serializer().setRegistryName(new ResourceLocation(Constants.MODID, "sulfur")));
    }
}