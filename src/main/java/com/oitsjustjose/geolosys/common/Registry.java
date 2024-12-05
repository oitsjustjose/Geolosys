package com.oitsjustjose.geolosys.common;

import com.google.common.collect.Lists;
import com.oitsjustjose.geolosys.common.blocks.OreBlock;
import com.oitsjustjose.geolosys.common.blocks.PeatBlock;
import com.oitsjustjose.geolosys.common.blocks.PlantBlock;
import com.oitsjustjose.geolosys.common.blocks.SampleBlock;
import com.oitsjustjose.geolosys.common.items.CoalItem;
import com.oitsjustjose.geolosys.common.items.ProPickItem;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;
import com.oitsjustjose.geolosys.common.world.feature.RemoveVeinsFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;

public class Registry {
    public final DeferredRegister<Block> BlockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public final DeferredRegister<Item> ItemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public final DeferredRegister<Feature<?>> FeatureRegistry = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID);
    public final DeferredRegister<CreativeModeTab> TabRegistry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    // Here because cutouts and coloring
    public final RegistryObject<Block> peat = BlockRegistry.register("peat", PeatBlock::new);
    public final RegistryObject<Block> rhododendron = BlockRegistry.register("rhododendron", () -> new PlantBlock(false, peat));

    // Creative tab registry things
    public RegistryObject<CreativeModeTab> CreativeTab;
    public final RegistryObject<Item> proPick = ItemRegistry.register("prospectors_pick", ProPickItem::new);

    private final List<RegistryObject<Block>> NeedItemBlocks = Lists.newArrayList();
    private final HashMap<String, Integer> UniversalMaterials = new HashMap<>() {{
        put("anthracite_coal", 2);
        put("autunite", 0);
        put("azurite", 0);
        put("bauxite", 0);
        put("beryl", 7);
        put("bituminous_coal", 2);
        put("cassiterite", 0);
        put("cinnabar", 0);
        put("coal", 2);
        put("galena", 0);
        put("gold", 0);
        put("hematite", 0);
        put("kimberlite", 7);
        put("lapis", 5);
        put("lignite", 2);
        put("limonite", 0);
        put("malachite", 0);
        put("platinum", 0);
        put("quartz", 5);
        put("sphalerite", 0);
        put("teallite", 0);
    }};


    public Registry() {
        RegisterBlocks();
        RegisterItems();
        RegisterWorldGen();
        RegisterCreativeTab();
    }

    public void RegisterAll(FMLJavaModLoadingContext ctx) {
        BlockRegistry.register(ctx.getModEventBus());
        ItemRegistry.register(ctx.getModEventBus());
        FeatureRegistry.register(ctx.getModEventBus());
        TabRegistry.register(ctx.getModEventBus());
    }

    private void RegisterBlocks() {
        var baseProps = Block.Properties.of().strength(5.0F, 10F).sound(SoundType.STONE).mapColor(MapColor.STONE).requiresCorrectToolForDrops();
        var dsProps = Block.Properties.of().strength(7.5F, 10F).sound(SoundType.DEEPSLATE).mapColor(MapColor.DEEPSLATE).requiresCorrectToolForDrops();
        var netherProps = Block.Properties.of().strength(7.5F, 10F).sound(SoundType.STONE).mapColor(MapColor.NETHER).requiresCorrectToolForDrops();

        // Non-standard blocks
        NeedItemBlocks.add(this.peat);
        NeedItemBlocks.add(this.rhododendron);

        // Ores that don't need a deepslate variant
        NeedItemBlocks.add(BlockRegistry.register("ancient_debris_ore", () -> new OreBlock(netherProps.sound(SoundType.ANCIENT_DEBRIS), 0)));
        NeedItemBlocks.add(BlockRegistry.register("ancient_debris_ore_sample", SampleBlock::new));
        NeedItemBlocks.add(BlockRegistry.register("nether_gold_ore", () -> new OreBlock(netherProps.sound(SoundType.NETHER_GOLD_ORE), 1)));
        NeedItemBlocks.add(BlockRegistry.register("nether_gold_ore_sample", SampleBlock::new));

        UniversalMaterials.forEach((name, xp) -> {
            NeedItemBlocks.add(BlockRegistry.register(name + "_ore", () -> new OreBlock(baseProps, xp)));
            NeedItemBlocks.add(BlockRegistry.register("deepslate_" + name + "_ore", () -> new OreBlock(dsProps, xp)));
            NeedItemBlocks.add(BlockRegistry.register(name + "_ore_sample", SampleBlock::new));
        });
    }

    private void RegisterItems() {
        Item.Properties baseProps = new Item.Properties();

        // Register Block Items
        NeedItemBlocks.forEach(x -> ItemRegistry.register(x.getId().getPath(), () -> new BlockItem(x.get(), new Item.Properties())));

        // Coals
        ItemRegistry.register("anthracite_coal", () -> new CoalItem(20));
        ItemRegistry.register("bituminous_coal", () -> new CoalItem(16));
        ItemRegistry.register("lignite_coal", () -> new CoalItem(12));
        ItemRegistry.register("peat_coal", () -> new CoalItem(6));
        ItemRegistry.register("bituminous_coal_coke", () -> new CoalItem(32));
        ItemRegistry.register("lignite_coal_coke", () -> new CoalItem(24));

        // Ingots
        ItemRegistry.register("aluminum_ingot", () -> new Item(baseProps));
        ItemRegistry.register("lead_ingot", () -> new Item(baseProps));
        ItemRegistry.register("nickel_ingot", () -> new Item(baseProps));
        ItemRegistry.register("platinum_ingot", () -> new Item(baseProps));
        ItemRegistry.register("silver_ingot", () -> new Item(baseProps));
        ItemRegistry.register("tin_ingot", () -> new Item(baseProps));
        ItemRegistry.register("zinc_ingot", () -> new Item(baseProps));

        // Nuggets
        ItemRegistry.register("aluminum_nugget", () -> new Item(baseProps));
        ItemRegistry.register("copper_nugget", () -> new Item(baseProps));
        ItemRegistry.register("lead_nugget", () -> new Item(baseProps));
        ItemRegistry.register("nickel_nugget", () -> new Item(baseProps));
        ItemRegistry.register("platinum_nugget", () -> new Item(baseProps));
        ItemRegistry.register("silver_nugget", () -> new Item(baseProps));
        ItemRegistry.register("tin_nugget", () -> new Item(baseProps));
        ItemRegistry.register("zinc_nugget", () -> new Item(baseProps));

        // Clusters
        ItemRegistry.register("aluminum_cluster", () -> new Item(baseProps));
        ItemRegistry.register("ancient_debris_cluster", () -> new Item(baseProps));
        ItemRegistry.register("copper_cluster", () -> new Item(baseProps));
        ItemRegistry.register("gold_cluster", () -> new Item(baseProps));
        ItemRegistry.register("iron_cluster", () -> new Item(baseProps));
        ItemRegistry.register("lead_cluster", () -> new Item(baseProps));
        ItemRegistry.register("nether_gold_cluster", () -> new Item(baseProps));
        ItemRegistry.register("nickel_cluster", () -> new Item(baseProps));
        ItemRegistry.register("osmium_cluster", () -> new Item(baseProps));
        ItemRegistry.register("platinum_cluster", () -> new Item(baseProps));
        ItemRegistry.register("silver_cluster", () -> new Item(baseProps));
        ItemRegistry.register("tin_cluster", () -> new Item(baseProps));
        ItemRegistry.register("uranium_cluster", () -> new Item(baseProps));
        ItemRegistry.register("zinc_cluster", () -> new Item(baseProps));
    }

    public void RegisterWorldGen() {
        FeatureRegistry.register("deposits", () -> new DepositFeature(NoneFeatureConfiguration.CODEC));
        FeatureRegistry.register("remove_veins", () -> new RemoveVeinsFeature(NoneFeatureConfiguration.CODEC));
    }

    public void RegisterCreativeTab() {
        CreativeTab = TabRegistry.register("items", () -> CreativeModeTab.builder().icon(() -> new ItemStack(proPick.get())).title(Component.translatable("itemGroup." + Constants.MOD_ID + ".name")).displayItems((params, output) -> {
            var items = ForgeRegistries.ITEMS.getKeys().stream().filter(x -> x.getNamespace().equals(Constants.MOD_ID));

            items.sorted((a, b) -> {
                var x = ForgeRegistries.ITEMS.getValue(a);
                var y = ForgeRegistries.ITEMS.getValue(b);

                if (x != null && y != null) {
                    return x.getClass().toString().compareTo(y.getClass().toString());
                }

                return a.compareTo(b);
            }).forEach(key -> { // Add to tab
                var item = ForgeRegistries.ITEMS.getValue(key);
                if (item != null) {
                    output.accept(new ItemStack(item));
                }
            });
        }).build());
    }
}