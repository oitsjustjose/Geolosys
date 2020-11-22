// package com.oitsjustjose.geolosys.common.compat;

// import javax.annotation.Nonnull;

// import com.oitsjustjose.geolosys.common.compat.modules.CoalVariants;
// import com.oitsjustjose.geolosys.common.compat.modules.OsmiumDrop;
// import com.oitsjustjose.geolosys.common.compat.modules.Sulfur;
// import com.oitsjustjose.geolosys.common.compat.modules.YelloriumDrop;
// import com.oitsjustjose.geolosys.common.utils.Constants;

// import net.minecraft.util.ResourceLocation;
// import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
// import net.minecraftforge.event.RegistryEvent;
// import net.minecraftforge.eventbus.api.SubscribeEvent;
// import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

// @EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
// public class CompatLoader {
//     @SubscribeEvent
//     public static void registerModifierSerializers(
//             @Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
//         // Osmium compat for ore and sample
//         event.getRegistry().register(
//                 new OsmiumDrop.Serializer().setRegistryName(new ResourceLocation(Constants.MODID, "osmium_drop")));
//         event.getRegistry().register(new OsmiumDrop.Serializer()
//                 .setRegistryName(new ResourceLocation(Constants.MODID, "osmium_drop_sample")));

//         // Yellorium compat for ore and sample
//         event.getRegistry().register(new YelloriumDrop.Serializer()
//                 .setRegistryName(new ResourceLocation(Constants.MODID, "yellorium_drop")));
//         event.getRegistry().register(new YelloriumDrop.Serializer()
//                 .setRegistryName(new ResourceLocation(Constants.MODID, "yellorium_drop_sample")));

//         // Sulfur drop (ore only)
//         event.getRegistry()
//                 .register(new Sulfur.Serializer().setRegistryName(new ResourceLocation(Constants.MODID, "sulfur")));
//         // Extra coal drops
//         event.getRegistry().register(
//                 new CoalVariants.Serializer().setRegistryName(new ResourceLocation(Constants.MODID, "coals")));
//     }
// }