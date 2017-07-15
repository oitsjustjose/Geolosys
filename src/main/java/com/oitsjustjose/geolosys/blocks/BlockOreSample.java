package com.oitsjustjose.geolosys.blocks;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.util.Lib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockGlass;
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
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockOreSample extends Block
{
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

    public BlockOreSample()
    {
        super(Material.GROUND);
        this.setRegistryName(new ResourceLocation(Lib.MODID, "ore_sample"));
        this.setHardness(2.0F);
        this.setResistance(10F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.HEMATITE));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        ForgeRegistries.BLOCKS.register(this);
        ForgeRegistries.ITEMS.register(new ItemBlockOre(this));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0.2F, 0.0F, 0.2F, 0.8F, 0.25F, 0.8F);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Geolosys.CLUSTER;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return false;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        int meta = state.getBlock().getMetaFromState(state);
        switch (meta)
        {
            case 0:
                return ItemCluster.META_IRON;
            case 1:
                return ItemCluster.META_IRON;
            case 2:
                return ItemCluster.META_COPPER;
            case 3:
                return ItemCluster.META_COPPER;
            case 4:
                return ItemCluster.META_TIN;
            case 5:
                return ItemCluster.META_TIN;
            case 6:
                return ItemCluster.META_SILVER;
            case 7:
                return ItemCluster.META_ALUMINUM;
            case 8:
                return ItemCluster.META_PLATINUM;
            case 9:
                return ItemCluster.META_URANIUM;
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
        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
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

    public enum EnumType implements IStringSerializable
    {
        HEMATITE(0, 1, "hematite", "hematite"),
        LIMONITE(1, 2, "limonite", "limonite"),
        MALACHITE(2, 1, "malachite", "malachite"),
        AZURITE(3, 1, "azurite", "azurite"),
        CASSITERITE(4, 1, "cassiterite", "cassiterite"),
        TEALLITE(5, 1, "teallite", "teallite"),
        GALENA(6, 2, "galena", "galena"),
        BAUXITE(7, 0, "bauxite", "bauxite"),
        PLATINUM(8, 2, "platinum", "platinum"),
        AUTUNITE(9, 2, "autunite", "autunite");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        private final int toolLevel;
        private final String serializedName;
        private final String unlocalizedName;

        EnumType(int meta, int toolLevel, String name, String unlocalizedName)
        {
            this.meta = meta;
            this.toolLevel = toolLevel;
            this.serializedName = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getToolLevel()
        {
            return this.toolLevel;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public static EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.serializedName;
        }

        static
        {
            for (EnumType type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
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
            return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "." + EnumType.byMetadata(stack.getMetadata()).getName();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
        {
            if (this.isInCreativeTab(tab))
                for (int i = 0; i < EnumType.values().length; ++i)
                    list.add(new ItemStack(this, 1, i));
        }

        private void registerModels()
        {
            for (int i = 0; i < EnumType.values().length; i++)
                Geolosys.clientRegistry.register(new ItemStack(this, 1, i), VARIANT.getName() + "=" + EnumType.byMetadata(i).getName());
        }
    }
}
