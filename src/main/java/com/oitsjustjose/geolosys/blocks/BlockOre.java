package com.oitsjustjose.geolosys.blocks;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.Lib;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockOre extends Block
{

	public static final PropertyEnum<BlockOre.EnumType> VARIANT = PropertyEnum.<BlockOre.EnumType> create("variant", BlockOre.EnumType.class);

	public BlockOre()
	{
		super(Material.ROCK);
		this.setRegistryName(new ResourceLocation(Lib.MODID, "ore"));
		this.setHardness(7.5F);
		this.setResistance(10F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockOre.EnumType.HEMATITE));
		this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
		ForgeRegistries.BLOCKS.register(this);
		ForgeRegistries.ITEMS.register(new ItemBlockOre(this));
//		this.registerModels();
	}
	


	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockOre.EnumType.byMetadata(meta));
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		if (this.getCreativeTabToDisplayOn() == itemIn)
		{
			for (int i = 0; i < BlockOre.EnumType.values().length; i++)
			{
				items.add(new ItemStack(Geolosys.ore, 1, i));
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockOre.EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((BlockOre.EnumType) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { VARIANT });
	}

	public static enum EnumType implements IStringSerializable
	{
		HEMATITE(0, "hematite", "hematite"), LIMONITE(1, "limonite", "limonite"), MALACHITE(2, "malachite", "malachite"), LAZURITE(3, "lazurite", "lazurite"), CASSITERITE(4, "cassiterite", "cassiterite"), SPHALERITE(5, "sphalerite", "sphalerite"), GALENA(6, "galena", "galena"), BAUXITE(7, "bauxite", "bauxite");

		private static final BlockOre.EnumType[] META_LOOKUP = new BlockOre.EnumType[values().length];
		private final int meta;
		private final String serializedName;
		private final String unlocalizedName;

		private EnumType(int meta, String name, String unlocalizedName)
		{
			this.meta = meta;
			this.serializedName = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata()
		{
			return this.meta;
		}

		public String toString()
		{
			return this.unlocalizedName;
		}

		public static BlockOre.EnumType byMetadata(int meta)
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
			for (BlockOre.EnumType type : values())
			{
				META_LOOKUP[type.getMetadata()] = type;
			}
		}
	}

	public class ItemBlockOre extends ItemBlock
	{

		public ItemBlockOre(Block block)
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
			return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "." + BlockOre.EnumType.byMetadata(stack.getMetadata()).getName();
		}

		@Override
		public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
		{
			if (this.isInCreativeTab(tab))
			{
				this.block.getSubBlocks(tab, items);
			}
		}
		
		private void registerModels()
		{
			for (int i = 0; i < BlockOre.EnumType.values().length; i++)
			{
				Geolosys.clientRegistry.register(new ItemStack(this, 1, i), VARIANT.getName() + "=" + BlockOre.EnumType.byMetadata(i).getName());
			}
		}
	}
}
