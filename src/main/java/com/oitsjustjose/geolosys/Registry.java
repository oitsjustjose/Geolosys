package com.oitsjustjose.geolosys;

import com.oitsjustjose.geolosys.common.blocks.*;
import com.oitsjustjose.geolosys.common.items.CoalItem;
import com.oitsjustjose.geolosys.common.items.ProPickItem;
import com.oitsjustjose.geolosys.common.items.Types.Coals;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Registry {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static RegistryObject<Block> PEAT;
    public static RegistryObject<Block> RHODODENDRON;
    public static RegistryObject<Item> PRO_PICK;
    public static TagKey<Block> SUPPORTS_SAMPLE = BlockTags
            .create(new ResourceLocation(Constants.MODID, "supports_sample"));

    public static void register(IEventBus modbus) {
        registerBlocks();
        registerItems();
        BLOCKS.register(modbus);
        ITEMS.register(modbus);
    }

    private static void registerBlocks() {

        for (Types.Ores o : Types.Ores.values()) {
            registerOre(o);
        }

        for (Types.DeepslateOres o : Types.DeepslateOres.values()) {
            registerDeepslateOre(o);
        }

        PEAT = BLOCKS.register("peat",PeatBlock::new);
        ITEMS.register("peat",itemOf(PEAT));

        RHODODENDRON = BLOCKS.register("rhododendron",()->new PlantBlock(false, PEAT));
        ITEMS.register("rhododendron",itemOf(RHODODENDRON));
    }

    private static void registerItems() {
        Item.Properties genericItemProps = new Item.Properties().tab(GeolosysGroup.getInstance());

        String CLUSTER_POSTFIX = "_cluster";
        String INGOT_POSTFIX = "_ingot";
        String NUGGET_POSTFIX = "_nugget";
        String COAL_POSTFIX = "_coal";
        String COAL_COKE_POSTFIX = "_coal_coke";

        for (com.oitsjustjose.geolosys.common.items.Types.Clusters cluster : com.oitsjustjose.geolosys.common.items.Types.Clusters.values()) {
            RegistryObject<Item> item = ITEMS.register(cluster.getName() + CLUSTER_POSTFIX,
                    ()->new Item(genericItemProps));
            cluster.setItem(item);
        }
        for (com.oitsjustjose.geolosys.common.items.Types.Ingots ingot : com.oitsjustjose.geolosys.common.items.Types.Ingots.values()) {
            RegistryObject<Item> item = ITEMS.register(ingot.getName() + INGOT_POSTFIX,
                    ()->new Item(genericItemProps));
            ingot.setItem(item);
        }
        for (com.oitsjustjose.geolosys.common.items.Types.Nuggets nugget : com.oitsjustjose.geolosys.common.items.Types.Nuggets.values()) {
            RegistryObject<Item> item = ITEMS.register(nugget.getName() + NUGGET_POSTFIX,
                    ()->new Item(genericItemProps));
            nugget.setItem(item);
        }
        // Init Coals
        for (Coals coal : Coals.values()) {
            RegistryObject<Item> item = ITEMS.register(coal.getName() + (coal.isCoalCoke() ? COAL_COKE_POSTFIX : COAL_POSTFIX),
                    ()->new CoalItem(coal));
            coal.setItem(item);
        }

        PRO_PICK = ITEMS.register("prospectors_pick", ProPickItem::new);
    }

    private static void registerOre(Types.Ores o) {
        BlockBehaviour.Properties stoneOreProperties = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                .strength(5.0F, 10F).sound(SoundType.STONE).requiresCorrectToolForDrops();

        final String ORE_REGISTRY_NAME = o.getUnlocalizedName().toLowerCase() + "_ore";
        final String SAMPLE_REGISTRY_NAME = o.getUnlocalizedName().toLowerCase() + "_ore_sample";

        RegistryObject<Block> block = BLOCKS.register(ORE_REGISTRY_NAME, ()->new OreBlock(stoneOreProperties, o.getXp()));
        RegistryObject<Block> sample = BLOCKS.register(SAMPLE_REGISTRY_NAME, SampleBlock::new);
        o.setBlock(block);
        o.setSample(sample);

        ITEMS.register(ORE_REGISTRY_NAME, itemOf(block));
        ITEMS.register(SAMPLE_REGISTRY_NAME, itemOf(sample));
    }

    private static void registerDeepslateOre(Types.DeepslateOres o) {
        BlockBehaviour.Properties deepslateOreProperties = BlockBehaviour.Properties
                .of(Material.STONE, MaterialColor.DEEPSLATE)
                .strength(7.5F, 10F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops();

        final String ORE_REGISTRY_NAME = o.getUnlocalizedName().toLowerCase() + "_ore";
        RegistryObject<Block> block = BLOCKS.register(ORE_REGISTRY_NAME, ()->new OreBlock(deepslateOreProperties, o.getXp()));

        o.setBlock(block);
        o.setSample(o.getOrigin().getSample());

        ITEMS.register(ORE_REGISTRY_NAME, itemOf(block));
    }

    private static Supplier<Item> itemOf(RegistryObject<Block> block) {
        return ()->new BlockItem(block.get(), new Item.Properties().tab(GeolosysGroup.getInstance()));
    }
}
