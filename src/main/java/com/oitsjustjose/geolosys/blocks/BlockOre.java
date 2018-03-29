package com.oitsjustjose.geolosys.blocks;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.config.Config;
import com.oitsjustjose.geolosys.items.ItemCluster;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

import java.util.Random;

public class BlockOre extends Block
{
    public static final PropertyEnum<Types.Modded> VARIANT = PropertyEnum.create("variant", Types.Modded.class);

    public BlockOre()
    {
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
    }

    private void setHarvestLevels()
    {
        for (Types.Modded t : Types.Modded.values())
        {
            this.setHarvestLevel("pickaxe", t.getToolLevel(), this.getDefaultState().withProperty(VARIANT, t));
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Geolosys.getInstance().CLUSTER;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        Config config = Config.getInstance();
        Item CLUSTER = Geolosys.getInstance().CLUSTER;
        // Special case for Limonite; odd-chance for the drop to be nickel AND iron
        if (state.getBlock().getMetaFromState(state) == 1)
        {
            // Studies say that 2% of Limonite is Nickel, but this is Minecraft; buffed to 20%:
            Random rand = new Random();
            int rng = rand.nextInt(5);
            if (rng == 0)
            {
                drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_NICKEL));
            }
            else
            {
                drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_IRON));
            }
        }
        // Special case for Galena; silver OR lead will be dropped for sure, maybe both!
        else if (state.getBlock().getMetaFromState(state) == 6)
        {
            Random rand = new Random();
            int rng = rand.nextInt(2);
            if (rng == 0)
            {
                drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_SILVER));
                rng = rand.nextInt(2);
                if (rng == 0)
                {
                    drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_LEAD));
                }
            }
            else
            {
                drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_LEAD));
            }
        }
        // Special case for Osmium
        else if (state.getBlock().getMetaFromState(state) == 8)
        {
            if (config.enableOsmiumExclusively)
            {
                drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_OSMIUM));
            }
            else if (config.enableOsmium)
            {
                Random rand = new Random();
                int rng = rand.nextInt(2);
                if (rng == 0)
                {
                    drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_PLATINUM));
                }
                else
                {
                    drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_OSMIUM));
                }
            }
            else
            {
                drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_PLATINUM));
            }
        }
        // Special case for Autunite to drop yellorium
        else if (state.getBlock().getMetaFromState(state) == 9)
        {
            if (config.enableYellorium)
            {
                Random rand = new Random();
                int rng = rand.nextInt(2);
                if (rng == 0)
                {
                    drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_URANIUM));
                }
                else
                {
                    drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_YELLORIUM));
                }
            }
            else
            {
                drops.add(new ItemStack(CLUSTER, 1, ItemCluster.META_URANIUM));
            }
        }
        else
        {
            drops.add(new ItemStack(CLUSTER, 1, this.damageDropped(state)));
        }
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return state.getBlock().getMetaFromState(state) == 0 || state.getBlock().getMetaFromState(state) == 1;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        switch (blockState.getBlock().getMetaFromState(state))
        {
            case 0:
                return new ItemStack(Blocks.IRON_ORE);
            case 1:
                return new ItemStack(Blocks.IRON_ORE);
            default:
                return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int meta = state.getBlock().getMetaFromState(state);
        switch (meta)
        {
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(state.getBlock(), 1, this.getMetaFromState(state));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getDefaultState().withProperty(VARIANT, Types.Modded.byMetadata(meta));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, Types.Modded.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT);
    }

    /**
     * An ItemBlock class for this block allowing it to
     * support subtypes with proper placement
     */
    public class ItemBlockOre extends ItemBlock
    {

        ItemBlockOre(Block block)
        {
            super(block);
            this.setHasSubtypes(true);
            this.setRegistryName(block.getRegistryName());
            this.setMaxDamage(0);
            this.registerModels();
        }

        @Override
        public int getMetadata(int damage)
        {
            return damage;
        }

        @Override
        public String getUnlocalizedName(ItemStack stack)
        {
            return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "." + Types.Modded.byMetadata(stack.getMetadata()).getName();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
        {
            if (this.isInCreativeTab(tab))
            {
                for (int i = 0; i < Types.Modded.values().length; ++i)
                {
                    list.add(new ItemStack(this, 1, i));
                }
            }
        }

        private void registerModels()
        {
            for (int i = 0; i < Types.Modded.values().length; i++)
            {
                Geolosys.getInstance().clientRegistry.register(new ItemStack(this, 1, i), VARIANT.getName() + "=" + Types.Modded.byMetadata(i).getName());
            }
        }
    }
}
