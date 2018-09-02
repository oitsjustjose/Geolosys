package com.oitsjustjose.geolosys.common.blocks;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.compat.ModMaterials;
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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Random;

public class BlockOreVanilla extends Block
{
    public static final PropertyEnum<Types.Vanilla> VARIANT = PropertyEnum.create("variant", Types.Vanilla.class);


    public BlockOreVanilla()
    {
        super(Material.ROCK);
        this.setRegistryName(new ResourceLocation(Geolosys.MODID, "ore_vanilla"));
        this.setHardness(7.5F);
        this.setResistance(10F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Types.Vanilla.COAL));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        this.setHarvestLevels();
        ForgeRegistries.BLOCKS.register(this);
        ForgeRegistries.ITEMS.register(new ItemBlockOre(this));
        this.registerOreDict();
    }

    private void registerOreDict()
    {
        for (int i = 0; i < Types.Vanilla.values().length; i++)
        {
            OreDictionary.registerOre("oreBlock" + Types.Vanilla.values()[i].getName().substring(0, 1).toUpperCase() + Types.Vanilla.values()[i].getName().substring(1), new ItemStack(this, 1, i));
        }
    }

    private void setHarvestLevels()
    {
        for (Types.Vanilla t : Types.Vanilla.values())
        {
            this.setHarvestLevel("pickaxe", t.getToolLevel(), this.getDefaultState().withProperty(VARIANT, t));
        }
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return true;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        switch (blockState.getBlock().getMetaFromState(state))
        {
            case 0:
                return new ItemStack(Blocks.COAL_ORE, 1);
            case 1:
                return new ItemStack(Blocks.REDSTONE_ORE, 1);
            case 2:
                return new ItemStack(Blocks.GOLD_ORE, 1);
            case 3:
                return new ItemStack(Blocks.LAPIS_ORE, 1);
            case 4:
                return new ItemStack(Blocks.QUARTZ_ORE, 1);
            case 5:
                return new ItemStack(Blocks.DIAMOND_ORE, 1);
            case 6:
                return new ItemStack(Blocks.EMERALD_ORE, 1);
            default:
                return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        Random random = new Random();
        int meta = state.getBlock().getMetaFromState(state);
        if (meta == 0)
        {
            drops.add(new ItemStack(Blocks.COAL_ORE.getItemDropped(state, random, fortune), Blocks.COAL_ORE.quantityDroppedWithBonus(fortune, random), Blocks.COAL_ORE.damageDropped(state)));
            if (ModConfig.featureControl.enableCoals)
            {
                /**************************
                 *       Coal types       *
                 **************************/
                Item coal = Geolosys.getInstance().COAL;
                int y = pos.getY();
                int rng = random.nextInt(10) + fortune;
                if (y <= 12)
                {
                    if (rng > 5)
                    {
                        drops.add(new ItemStack(coal, 1, 3));
                    }
                }
                else if (y <= 24)
                {
                    if (rng > 5)
                    {
                        drops.add(new ItemStack(coal, 1, 2));
                    }
                }
                else if (y < 36)
                {
                    if (rng > 5)
                    {
                        drops.add(new ItemStack(coal, 1, 1));
                    }
                }
                else if (y < 48)
                {
                    if (rng > 5)
                    {
                        drops.add(new ItemStack(coal, 1, 0));
                    }
                }
            }
            /**************************
             *      Nuclearcraft      *
             **************************/
            if (ModMaterials.NC_GEM != null)
            {
                int rng = random.nextInt(100);
                if (rng < 18)
                {
                    drops.add(new ItemStack(ModMaterials.NC_DUST, 1, 9));
                }
            }
            /**************************
             *         Sulfur         *
             **************************/
            if (ModConfig.featureControl.enableSulfur && OreDictionary.doesOreNameExist("dustSulfur") && OreDictionary.getOres("dustSulfur").size() > 0)
            {
                int rng = random.nextInt(50);
                if (rng == 0)
                {
                    drops.add(OreDictionary.getOres("dustSulfur").get(0));
                }
            }
        }
        else if (meta == 1)
        {
            drops.add(new ItemStack(Blocks.REDSTONE_ORE.getItemDropped(state, random, fortune), Blocks.REDSTONE_ORE.quantityDroppedWithBonus(fortune, random), Blocks.REDSTONE_ORE.damageDropped(state)));
            int rng = random.nextInt(60);
            /**************************
             *          ExU2          *
             **************************/
            if (ModMaterials.EXU_MATERIAL != null && rng < 2)
            {
                drops.add(new ItemStack(ModMaterials.EXU_MATERIAL));
            }
            /**************************
             *           TE           *
             **************************/
            if (ModMaterials.TE_MATERIAL != null && rng == 2)
            {
                drops.add(new ItemStack(ModMaterials.TE_MATERIAL, 1, 866));
            }
            /**************************
             *      Nuclearcraft      *
             **************************/
            if (ModMaterials.NC_GEM != null)
            {
                rng = random.nextInt(100);
                if (rng < 25)
                {
                    drops.add(new ItemStack(ModMaterials.NC_GEM, 1, 0));
                }
            }
        }
        else if (meta == 2)
        {
            drops.add(new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_GOLD));
        }
        else if (meta == 3)
        {
            drops.add(new ItemStack(Blocks.LAPIS_ORE.getItemDropped(state, random, fortune), Blocks.LAPIS_ORE.quantityDroppedWithBonus(fortune, random), Blocks.LAPIS_ORE.damageDropped(state)));
            /**************************
             *      Nuclearcraft      *
             **************************/
            if (ModMaterials.NC_GEM != null)
            {
                int rng = random.nextInt(100);
                if (rng < 95)
                {
                    drops.add(new ItemStack(ModMaterials.NC_GEM, random.nextInt(2) + 1, 2));
                }
            }
        }
        else if (meta == 4)
        {
            drops.add(new ItemStack(Blocks.QUARTZ_ORE.getItemDropped(state, random, fortune), Blocks.QUARTZ_ORE.quantityDroppedWithBonus(fortune, random), Blocks.QUARTZ_ORE.damageDropped(state)));
            int fortuneDropCalc = 1 + random.nextInt(fortune + 1);
            for (int i = 0; i < fortuneDropCalc; i++)
            {
                int rng = random.nextInt(25);
                /**************************
                 *           AE           *
                 **************************/
                if (ModMaterials.AE_MATERIAL != null)
                {
                    if (rng < 5)
                    {
                        drops.add(new ItemStack(ModMaterials.AE_MATERIAL, 1, 0));
                    }
                    else if (rng > 5 && rng < 7)
                    {
                        drops.add(new ItemStack(ModMaterials.AE_MATERIAL, 1, 1));
                    }
                }
                /**************************
                 *           AA           *
                 **************************/
                if (ModMaterials.BLACK_QUARTZ != null)
                {
                    if (rng >= 6 && rng < 10)
                    {
                        drops.add(new ItemStack(ModMaterials.BLACK_QUARTZ, 1, 5));
                    }
                }
                /**************************
                 *      Nuclearcraft      *
                 **************************/
                if (ModMaterials.NC_GEM != null)
                {
                    rng = random.nextInt(100);
                    if (rng < 18)
                    {
                        drops.add(new ItemStack(ModMaterials.NC_DUST, 1, 10));
                    }
                }
            }
        }
        else if (meta == 5)
        {
            drops.add(new ItemStack(Blocks.DIAMOND_ORE.getItemDropped(state, random, fortune), Blocks.DIAMOND_ORE.quantityDroppedWithBonus(fortune, random), Blocks.DIAMOND_ORE.damageDropped(state)));
        }
        else if (meta == 6)
        {
            drops.add(new ItemStack(Blocks.EMERALD_ORE.getItemDropped(state, random, fortune), Blocks.EMERALD_ORE.quantityDroppedWithBonus(fortune, random), Blocks.EMERALD_ORE.damageDropped(state)));
        }
    }


    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        switch (state.getBlock().getMetaFromState(state))
        {
            case 0:
                return Blocks.COAL_ORE.getExpDrop(state, world, pos, fortune);
            case 1:
                return Blocks.REDSTONE_ORE.getExpDrop(state, world, pos, fortune);
            case 3:
                return Blocks.LAPIS_ORE.getExpDrop(state, world, pos, fortune);
            case 4:
                return Blocks.QUARTZ_ORE.getExpDrop(state, world, pos, fortune);
            case 5:
                return Blocks.DIAMOND_ORE.getExpDrop(state, world, pos, fortune);
            case 6:
                return Blocks.EMERALD_ORE.getExpDrop(state, world, pos, fortune);
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
        return this.getDefaultState().withProperty(VARIANT, Types.Vanilla.byMetadata(meta));
    }

    @Override
    @SuppressWarnings("deprecation")
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
            return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "." + Types.Vanilla.byMetadata(stack.getMetadata()).getName();
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
                Geolosys.getInstance().clientRegistry.register(new ItemStack(this, 1, i), VARIANT.getName() + "=" + Types.Vanilla.byMetadata(i).getName());
            }
        }
    }
}
