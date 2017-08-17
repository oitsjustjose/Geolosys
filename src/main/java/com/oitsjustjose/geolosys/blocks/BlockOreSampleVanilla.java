package com.oitsjustjose.geolosys.blocks;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.util.Lib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockOreSampleVanilla extends Block
{
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

    public BlockOreSampleVanilla()
    {
        super(Material.GROUND);
        this.setRegistryName(new ResourceLocation(Lib.MODID, "ore_sample_vanilla"));
        this.setHardness(0.125F);
        this.setResistance(2F);
        this.setSoundType(SoundType.GROUND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.COAL));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        ForgeRegistries.BLOCKS.register(this);
        ForgeRegistries.ITEMS.register(new ItemBlockOre(this));
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canPlaceBlockAt(worldIn, pos))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
        playerIn.swingArm(EnumHand.MAIN_HAND);
        return true;
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
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        Random random = new Random();
        int meta = state.getBlock().getMetaFromState(state);
        if (meta == 0)
            drops.add(new ItemStack(Blocks.COAL_ORE.getItemDropped(state, random, fortune), 1, Blocks.COAL_ORE.damageDropped(state)));
        else if (meta == 1)
            drops.add(new ItemStack(Blocks.REDSTONE_ORE.getItemDropped(state, random, fortune), 1, Blocks.REDSTONE_ORE.damageDropped(state)));
        else if (meta == 2)
            drops.add(new ItemStack(Geolosys.CLUSTER, 1, ItemCluster.META_GOLD));
        else if (meta == 3)
            drops.add(new ItemStack(Blocks.LAPIS_ORE.getItemDropped(state, random, fortune), 1, Blocks.LAPIS_ORE.damageDropped(state)));
        else if (meta == 4)
            drops.add(new ItemStack(Blocks.QUARTZ_ORE.getItemDropped(state, random, fortune), 1, Blocks.QUARTZ_ORE.damageDropped(state)));
        else if (meta == 5)
            drops.add(new ItemStack(Blocks.DIAMOND_ORE.getItemDropped(state, random, fortune), 1, Blocks.DIAMOND_ORE.damageDropped(state)));
    }


    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return false;
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
        COAL(0, 0, "coal"),
        CINNABAR(1, 2, "cinnabar"),
        GOLD(2, 2, "gold"),
        LAPIS(3, 1, "lapis"),
        QUARTZ(4, 1, "quartz"),
        KIMBERLITE(5, 2, "kimberlite");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        private final int toolLevel;
        private final String unlocalizedName;

        EnumType(int meta, int toolLevel, String name)
        {
            this.meta = meta;
            this.toolLevel = toolLevel;
            this.unlocalizedName = name;
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
            return this.unlocalizedName;
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
