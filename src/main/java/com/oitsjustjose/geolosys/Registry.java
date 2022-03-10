package com.oitsjustjose.geolosys;

import com.oitsjustjose.geolosys.common.blocks.OreBlock;
import com.oitsjustjose.geolosys.common.blocks.PeatBlock;
import com.oitsjustjose.geolosys.common.blocks.PlantBlock;
import com.oitsjustjose.geolosys.common.blocks.SampleBlock;
import com.oitsjustjose.geolosys.common.blocks.Types;
import com.oitsjustjose.geolosys.common.items.CoalItem;
import com.oitsjustjose.geolosys.common.items.ProPickItem;
import com.oitsjustjose.geolosys.common.items.Types.Clusters;
import com.oitsjustjose.geolosys.common.items.Types.Coals;
import com.oitsjustjose.geolosys.common.items.Types.Ingots;
import com.oitsjustjose.geolosys.common.items.Types.Nuggets;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {
    public static Block PEAT;
    public static Block RHODODENDRON;
    public static Item PRO_PICK;
    public static TagKey<Block> SUPPORTS_SAMPLE = BlockTags
            .create(new ResourceLocation(Constants.MODID, "supports_sample"));

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        BlockBehaviour.Properties stoneOreProperties = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(5.0F, 10F).sound(SoundType.STONE).requiresCorrectToolForDrops();
        BlockBehaviour.Properties deepslateOreProperties = BlockBehaviour.Properties
                .of(Material.STONE, MaterialColor.DEEPSLATE)
                .strength(7.5F, 10F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops();

        for (Types.Ores o : Types.Ores.values()) {
            final String ORE_REGISTRY_NAME = o.getUnlocalizedName().toLowerCase() + "_ore";
            final String SAMPLE_REGISTRY_NAME = o.getUnlocalizedName().toLowerCase() + "_ore_sample";

            Block block = new OreBlock(stoneOreProperties, o.getXp())
                    .setRegistryName(new ResourceLocation(Constants.MODID, ORE_REGISTRY_NAME));
            Block sample = new SampleBlock()
                    .setRegistryName(new ResourceLocation(Constants.MODID, SAMPLE_REGISTRY_NAME));
            event.getRegistry().register(block);
            event.getRegistry().register(sample);
            o.setBlock(block);
            o.setSample(sample);
        }

        for (Types.DeepslateOres o : Types.DeepslateOres.values()) {
            final String ORE_REGISTRY_NAME = o.getUnlocalizedName().toLowerCase() + "_ore";
            Block block = new OreBlock(deepslateOreProperties, o.getXp())
                    .setRegistryName(new ResourceLocation(Constants.MODID, ORE_REGISTRY_NAME));
            event.getRegistry().register(block);

            o.setBlock(block);
            o.setSample(o.getOrigin().getSample());
        }

        Registry.PEAT = new PeatBlock().setRegistryName(Constants.MODID, "peat");
        event.getRegistry().register(PEAT);

        Registry.RHODODENDRON = new PlantBlock(false, Registry.PEAT).setRegistryName(Constants.MODID, "rhododendron");
        event.getRegistry().register(RHODODENDRON);
    }

    @SubscribeEvent
    public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
        Item.Properties genericItemProps = new Item.Properties().tab(GeolosysGroup.getInstance());

        // Init Block-Items
        ForgeRegistries.BLOCKS.getValues().parallelStream()
                .filter(x -> x.getRegistryName().getNamespace().equals(Constants.MODID))
                .forEach(x -> event.getRegistry()
                        .register(new BlockItem(x, new Item.Properties().tab(GeolosysGroup.getInstance()))
                                .setRegistryName(x.getRegistryName())));

        String CLUSTER_POSTFIX = "_cluster";
        String INGOT_POSTFIX = "_ingot";
        String NUGGET_POSTFIX = "_nugget";
        String COAL_POSTFIX = "_coal";
        String COAL_COKE_POSTFIX = "_coal_coke";

        for (Clusters cluster : Clusters.values()) {
            Item item = new Item(genericItemProps).setRegistryName(Constants.MODID,
                    cluster.getName() + CLUSTER_POSTFIX);
            event.getRegistry().register(item);
            cluster.setItem(item);
        }
        for (Ingots ingot : Ingots.values()) {
            Item item = new Item(genericItemProps).setRegistryName(Constants.MODID, ingot.getName() + INGOT_POSTFIX);
            event.getRegistry().register(item);
            ingot.setItem(item);
        }
        for (Nuggets nugget : Nuggets.values()) {
            Item item = new Item(genericItemProps).setRegistryName(Constants.MODID, nugget.getName() + NUGGET_POSTFIX);
            event.getRegistry().register(item);
            nugget.setItem(item);
        }
        // Init Coals
        for (Coals coal : Coals.values()) {
            Item item = new CoalItem(coal).setRegistryName(Constants.MODID,
                    coal.getName() + (coal.isCoalCoke() ? COAL_COKE_POSTFIX : COAL_POSTFIX));
            event.getRegistry().register(item);
            coal.setItem(item);
        }

        Registry.PRO_PICK = new ProPickItem();
        event.getRegistry().register(Registry.PRO_PICK);
    }

    @SubscribeEvent
    public void onFuelRegistry(FurnaceFuelBurnTimeEvent fuelBurnoutEvent) {
        for (Coals c : Coals.values()) {
            if (fuelBurnoutEvent.getItemStack().getItem().equals(c.getItem())) {
                fuelBurnoutEvent.setBurnTime(c.getBurnTime() * 200);
            }
        }
    }
}
