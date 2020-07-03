package com.oitsjustjose.geolosys.common.blocks;

import java.util.Random;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.items.ItemCluster;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends Block {
    public static final PropertyEnum<Types.Modded> VARIANT = PropertyEnum.create("variant", Types.Modded.class);
    public static final String[] oreDictByMeta = new String[] { "oreIron", "oreNickel", "oreCopper", "oreCopper",
            "oreTin", "oreTin", "oreGalena", "oreAluminum", "orePlatinum", "oreUranium", "oreZinc" };

    public BlockOre() {
        super(Material.ROCK);
        this.setRegistryName(new ResourceLocation(Geolosys.MODID, "ore"));
        this.setHardness(7.5F);
        this.setResistance(10F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Types.Modded.HEMATITE));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        this.setHarvestLevels();
        ForgeRegistries.BLOCKS.register(this);
        ForgeRegistries.ITEMS.register(new ItemBlockOre(this));
        this.registerOreDict();
    }

    private void registerOreDict() {
        for (int i = 0; i < Types.Modded.values().length; i++) {
            OreDictionary.registerOre("oreBlock" + Types.Modded.values()[i].getName().substring(0, 1).toUpperCase()
                    + Types.Modded.values()[i].getName().substring(1), new ItemStack(this, 1, i));
        }
    }

    private void setHarvestLevels() {
        for (Types.Modded t : Types.Modded.values()) {
            this.setHarvestLevel("pickaxe", t.getToolLevel(), this.getDefaultState().withProperty(VARIANT, t));
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Geolosys.getInstance().CLUSTER;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
            int fortune) {
        Item CLUSTER = Geolosys.getInstance().CLUSTER;
        // Special case for Limonite; odd-chance for the drop to be nickel AND iron
        if (state.getBlock().getMetaFromState(state) == 1) {
            // Studies say that 2% of Limonite is Nickel, but this is Minecraft; buffed to
            // 20%:
            Random rand = new Random();
            int rng = ModConfig.featureControl.enableFortuneOnAllOres ? rand.nextInt(5 - Math.min(3, fortune))
                    : rand.nextInt(5);
            int count = fortune > 0
                    ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1) : 1)
                    : 1;
            if (rng == 0) {
                drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_NICKEL));
            } else {
                drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_IRON));
            }
        }
        // Special case for Galena; silver OR lead will be dropped for sure, maybe both!
        else if (state.getBlock().getMetaFromState(state) == 6) {
            Random rand = new Random();
            boolean rng = rand.nextBoolean();
            int count = fortune > 0
                    ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1) : 1)
                    : 1;
            if (rng) {
                drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_SILVER));
                rng = rand.nextBoolean();
                if (rng) {
                    count = fortune > 0
                            ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1)
                                    : 1)
                            : 1;
                    drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_LEAD));
                }
            } else {
                drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_LEAD));
            }
        }
        // Special case for Osmium
        else if (state.getBlock().getMetaFromState(state) == 8) {
            if (ModConfig.compat.enableOsmiumExclusively) {
                Random rand = new Random();
                int count = fortune > 0
                        ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1) : 1)
                        : 1;
                drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_OSMIUM));
            } else if (ModConfig.compat.enableOsmium) {
                Random rand = new Random();
                boolean rng = rand.nextBoolean();
                int count = fortune > 0
                        ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1) : 1)
                        : 1;
                if (rng) {
                    drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_PLATINUM));
                } else {
                    drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_OSMIUM));
                }
            } else {
                Random rand = new Random();
                int count = fortune > 0
                        ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1) : 1)
                        : 1;
                drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_PLATINUM));
            }
        }
        // Special case for Autunite to drop yellorium
        else if (state.getBlock().getMetaFromState(state) == 9) {
            if (ModConfig.compat.enableYellorium) {
                Random rand = new Random();
                boolean rng = rand.nextBoolean();
                int count = fortune > 0
                        ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1) : 1)
                        : 1;
                if (rng) {
                    drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_URANIUM));
                } else {
                    drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_YELLORIUM));
                }
            } else {
                Random rand = new Random();
                int count = fortune > 0
                        ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1) : 1)
                        : 1;
                drops.add(new ItemStack(CLUSTER, count, ItemCluster.META_URANIUM));
            }
        } else {
            Random rand = new Random();
            int count = fortune > 0
                    ? (ModConfig.featureControl.enableFortuneOnAllOres ? Math.max(1, rand.nextInt(fortune) + 1) : 1)
                    : 1;
            drops.add(new ItemStack(CLUSTER, count, this.damageDropped(state)));
        }
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return !getSilkTouchDrop(state).isEmpty();
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        Random rand = new Random();
        int meta = state.getBlock().getMetaFromState(state);
        // Limonite:
        if (meta == 1) {
            if (rand.nextInt(5) == 0) {
                return getOreDictAlternative("oreNickel");
            } else {
                return getOreDictAlternative("oreIron");
            }
        }
        // Galena:
        else if (meta == 6) {
            if (rand.nextBoolean()) {
                return getOreDictAlternative("oreSilver");
            } else {
                return getOreDictAlternative("oreLead");
            }
        }
        // Platinum:
        else if (meta == 8) {
            if (ModConfig.compat.enableOsmiumExclusively) {
                return getOreDictAlternative("oreOsmium");
            } else if (ModConfig.compat.enableOsmium) {
                if (rand.nextBoolean()) {
                    return getOreDictAlternative("oreOsmium");
                } else {
                    return getOreDictAlternative("orePlatinum");
                }
            } else {
                return getOreDictAlternative("orePlatinum");
            }
        }
        // Autunite:
        else if (meta == 9) {
            if (ModConfig.compat.enableYellorium) {
                if (rand.nextBoolean()) {
                    return getOreDictAlternative("oreYellorium");
                } else {
                    return getOreDictAlternative("oreUranium");
                }
            } else {
                return getOreDictAlternative("oreUranium");
            }
        }
        return getOreDictAlternative(oreDictByMeta[meta]);
    }

    private ItemStack getOreDictAlternative(String oreName) {
        for (ItemStack i : OreDictionary.getOres(oreName)) {
            if (i.getItem() instanceof ItemCluster) {
                continue;
            }
            ItemStack retStack = i.copy();
            retStack.setCount(1);
            return retStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int damageDropped(IBlockState state) {
        int meta = state.getBlock().getMetaFromState(state);
        switch (meta) {
            case 0:
                return ItemCluster.META_IRON;
            case 2:
                return ItemCluster.META_COPPER;
            case 3:
                return ItemCluster.META_COPPER;
            case 4:
                return ItemCluster.META_TIN;
            case 5:
                return ItemCluster.META_TIN;
            case 7:
                return ItemCluster.META_ALUMINUM;
            case 8:
                return ItemCluster.META_PLATINUM;
            case 9:
                return ItemCluster.META_URANIUM;
            case 10:
                return ItemCluster.META_ZINC;
            default:
                return 0;
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
            EntityPlayer player) {
        return new ItemStack(state.getBlock(), 1, this.getMetaFromState(state));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(VARIANT, Types.Modded.byMetadata(meta));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, Types.Modded.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    /**
     * An ItemBlock class for this block allowing it to support subtypes with proper
     * placement
     */
    public class ItemBlockOre extends ItemBlock {

        ItemBlockOre(Block block) {
            super(block);
            this.setHasSubtypes(true);
            this.setRegistryName(block.getRegistryName());
            this.setMaxDamage(0);
            this.registerModels();
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

        @Override
        public String getUnlocalizedName(ItemStack stack) {
            return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "."
                    + Types.Modded.byMetadata(stack.getMetadata()).getName();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
            if (this.isInCreativeTab(tab)) {
                for (int i = 0; i < Types.Modded.values().length; ++i) {
                    list.add(new ItemStack(this, 1, i));
                }
            }
        }

        private void registerModels() {
            for (int i = 0; i < Types.Modded.values().length; i++) {
                Geolosys.getInstance().clientRegistry.register(new ItemStack(this, 1, i),
                        VARIANT.getName() + "=" + Types.Modded.byMetadata(i).getName());
            }
        }
    }
}
