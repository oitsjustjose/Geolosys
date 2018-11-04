package com.oitsjustjose.geolosys.common.blocks;

import java.util.ArrayList;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.util.Utils;

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
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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

public class BlockSampleVanilla extends Block
{
    public static final PropertyEnum<Types.Vanilla> VARIANT = PropertyEnum.create("variant", Types.Vanilla.class);
    private ArrayList<IBlockState> canPlaceBlacklist;

    public BlockSampleVanilla()
    {
        super(Material.GROUND);
        this.setRegistryName(new ResourceLocation(Geolosys.MODID, "ore_sample_vanilla"));
        this.setHardness(0.125F);
        this.setResistance(2F);
        this.setSoundType(SoundType.GROUND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Types.Vanilla.COAL));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        ForgeRegistries.BLOCKS.register(this);
        ForgeRegistries.ITEMS.register(new ItemBlockOre(this));
        MinecraftForge.EVENT_BUS.register(this);
        this.parsePlaceBlacklist();
    }

    private void parsePlaceBlacklist()
    {
        this.canPlaceBlacklist = new ArrayList<IBlockState>();
        for (String s : ModConfig.prospecting.samplePlaceBlacklist)
        {
            String[] parts = s.split(":");
            if (parts.length < 2 || parts.length > 3)
            {
                Geolosys.getInstance().LOGGER
                        .info("Entry " + s + " has incorrect formatting in samplePlaceBlacklist; skipping.");
                continue;
            }
            Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
            if (b == null)
            {
                Geolosys.getInstance().LOGGER.info("Entry " + s + " does not seem to exist; skipping.");
                continue;
            }
            if (parts.length == 3)
            {
                canPlaceBlacklist.add(Utils.getStateFromMeta(b, Integer.parseInt(parts[2])));
            }
            else
            {
                canPlaceBlacklist.add(b.getDefaultState());
            }
        }
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
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_,
            EnumFacing p_193383_4_)
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
        for (IBlockState state : canPlaceBlacklist)
        {
            if (Utils.doStatesMatch(state, worldIn.getBlockState(pos)))
            {
                return false;
            }
        }

        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (ModConfig.prospecting.boringSamples)
        {
            String resource = Types.Vanilla.byMetadata(state.getBlock().getMetaFromState(state)).getResource();
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
        if (ModConfig.prospecting.boringSamples)
        {
            drops.clear();
            return;
        }
        Geolosys.getInstance().ORE_VANILLA.getDrops(drops, world, pos, state, fortune);
        // Drop down the stack size:
        for (ItemStack stack : drops)
        {
            if (stack.getCount() > 1)
            {
                stack.setCount(1);
            }
        }
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
            EntityPlayer player)
    {
        return new ItemStack(state.getBlock(), 1, this.getMetaFromState(state));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
            float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return this.getDefaultState().withProperty(VARIANT, Types.Vanilla.byMetadata(meta));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, Types.Vanilla.byMetadata(meta));
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
        if (!ModConfig.prospecting.boringSamples || event.getHarvester() == null || event.getState() == null
                || event.getState().getBlock() != this)
        {
            return;
        }
        String resource = Types.Vanilla.byMetadata(event.getState().getBlock().getMetaFromState(event.getState()))
                .getResource();
        event.getHarvester().sendStatusMessage(new TextComponentString("You break the sample to find " + resource),
                true);
        event.getDrops().clear();
    }

    /**
     * An ItemBlock class for this block allowing it to support subtypes with proper placement
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
            return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "."
                    + Types.Vanilla.byMetadata(stack.getMetadata()).getName();
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
        {
            if (this.isInCreativeTab(tab))
            {
                for (int i = 0; i < Types.Vanilla.values().length; ++i)
                {
                    list.add(new ItemStack(this, 1, i));
                }
            }
        }

        private void registerModels()
        {
            for (int i = 0; i < Types.Vanilla.values().length; i++)
            {
                Geolosys.getInstance().clientRegistry.register(new ItemStack(this, 1, i),
                        VARIANT.getName() + "=" + Types.Vanilla.byMetadata(i).getName());
            }
        }
    }
}
