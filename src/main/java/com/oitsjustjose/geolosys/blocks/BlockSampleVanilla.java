package com.oitsjustjose.geolosys.blocks;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.util.Lib;
import net.minecraft.block.Block;
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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockSampleVanilla extends Block
{
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

    public BlockSampleVanilla()
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
        MinecraftForge.EVENT_BUS.register(this);
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
        if (Geolosys.getInstance().config.boringSamples)
        {
            String resource = EnumType.byMetadata(state.getBlock().getMetaFromState(state)).getResource();
            playerIn.sendStatusMessage(new TextComponentString("You break the sample to find " + resource), true);
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
        }
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
        if (Geolosys.getInstance().config.boringSamples)
        {
            drops.clear();
            return;
        }
        Random random = new Random();
        switch (state.getBlock().getMetaFromState(state))
        {
            case 0:
                drops.add(new ItemStack(Blocks.COAL_ORE.getItemDropped(state, random, fortune), 1, Blocks.COAL_ORE.damageDropped(state)));
                break;
            case 1:
                drops.add(new ItemStack(Blocks.REDSTONE_ORE.getItemDropped(state, random, fortune), 1, Blocks.REDSTONE_ORE.damageDropped(state)));
                break;
            case 2:
                drops.add(new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_GOLD));
                break;
            case 3:
                drops.add(new ItemStack(Blocks.LAPIS_ORE.getItemDropped(state, random, fortune), 1, Blocks.LAPIS_ORE.damageDropped(state)));
                break;
            case 4:
                drops.add(new ItemStack(Blocks.QUARTZ_ORE.getItemDropped(state, random, fortune), 1, Blocks.QUARTZ_ORE.damageDropped(state)));
                break;
            case 5:
                drops.add(new ItemStack(Blocks.DIAMOND_ORE.getItemDropped(state, random, fortune), 1, Blocks.DIAMOND_ORE.damageDropped(state)));
                break;
        }
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

    @SubscribeEvent
    public void registerEvent(BlockEvent.HarvestDropsEvent event)
    {
        if (!Geolosys.getInstance().config.boringSamples || event.getHarvester() == null || event.getState() == null || event.getState().getBlock() != this)
            return;
        String resource = EnumType.byMetadata(event.getState().getBlock().getMetaFromState(event.getState())).getResource();
        event.getHarvester().sendStatusMessage(new TextComponentString("You break the sample to find " + resource), true);
        event.getDrops().clear();
    }

    public enum EnumType implements IStringSerializable
    {
        COAL(0, "coal", "coal"),
        CINNABAR(1, "cinnabar", "redstone"),
        GOLD(2, "gold", "gold"),
        LAPIS(3, "lapis", "lapis"),
        QUARTZ(4, "quartz", "various quartz types"),
        KIMBERLITE(5, "kimberlite", "diamond");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];

        static
        {
            for (EnumType type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private final int meta;
        private final String name;
        private final String resource;

        EnumType(int meta, String name, String resource)
        {
            this.meta = meta;
            this.name = name;
            this.resource = resource;
        }

        public static EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }

        public String getResource()
        {
            return this.resource;
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
                Geolosys.getInstance().clientRegistry.register(new ItemStack(this, 1, i), VARIANT.getName() + "=" + EnumType.byMetadata(i).getName());
        }
    }
}
