package com.oitsjustjose.geolosys.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.blocks.BlockOreVanilla;
import com.oitsjustjose.geolosys.util.Lib;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemProPick extends Item
{
    public ItemProPick()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setRegistryName(new ResourceLocation(Lib.MODID, "PRO_PICK"));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        ForgeRegistries.ITEMS.register(this);
        this.registerModel();
    }

    private void registerModel()
    {
        Geolosys.clientRegistry.register(new ItemStack(this), new ResourceLocation(this.getRegistryName().toString()), "inventory");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return stack.getItem().getRegistryName().toString().replaceAll(":", ".");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        int xStart;
        int xEnd;
        int yStart;
        int yEnd;
        int zStart;
        int zEnd;
        boolean found = false;

        switch (facing)
        {
            case UP:
                xStart = -2;
                xEnd = 2;
                yStart = -5;
                yEnd = 0;
                zStart = -2;
                zEnd = 2;
                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (state.getBlock() instanceof BlockOre || state.getBlock() instanceof BlockOreVanilla)
                            {
                                foundMessage(player, state, facing);
                                found = true;
                                break;
                            }
                        }
                    }
                }
                break;
            case DOWN:
                xStart = -2;
                xEnd = 2;
                yStart = 0;
                yEnd = 5;
                zStart = -2;
                zEnd = 2;
                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (state.getBlock() instanceof BlockOre || state.getBlock() instanceof BlockOreVanilla)
                            {
                                foundMessage(player, state, facing);
                                found = true;
                                break;
                            }
                        }
                    }
                }
                break;
            case NORTH:
                xStart = -2;
                xEnd = 2;
                yStart = -2;
                yEnd = 2;
                zStart = 0;
                zEnd = 5;
                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            System.out.println("Checking: " + pos.add(x, y, z));
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (state.getBlock() instanceof BlockOre || state.getBlock() instanceof BlockOreVanilla)
                            {
                                foundMessage(player, state, facing);
                                found = true;
                                break;
                            }
                        }
                    }
                }
                break;
            case SOUTH:
                xStart = -2;
                xEnd = 2;
                yStart = -2;
                yEnd = 2;
                zStart = -5;
                zEnd = 0;

                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (state.getBlock() instanceof BlockOre || state.getBlock() instanceof BlockOreVanilla)
                            {
                                foundMessage(player, state, facing);
                                found = true;
                                break;
                            }
                        }
                    }
                }
                break;
            case EAST:
                xStart = -5;
                xEnd = 0;
                yStart = -2;
                yEnd = 2;
                zStart = -2;
                zEnd = 2;

                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (state.getBlock() instanceof BlockOre || state.getBlock() instanceof BlockOreVanilla)
                            {
                                foundMessage(player, state, facing);
                                found = true;
                                break;
                            }
                        }
                    }
                }
                break;
            case WEST:
                xStart = 0;
                xEnd = 5;
                yStart = -2;
                yEnd = 2;
                zStart = -2;
                zEnd = 2;
                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (state.getBlock() instanceof BlockOre || state.getBlock() instanceof BlockOreVanilla)
                            {
                                foundMessage(player, state, facing);
                                found = true;
                                break;
                            }
                        }
                    }
                }
                break;
        }


        if (!found)
        {
            player.sendStatusMessage(new TextComponentString("No deposits found"), true);
        }
        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
    }

    void foundMessage(EntityPlayer player, IBlockState state, EnumFacing facing)
    {
        player.sendStatusMessage(new TextComponentString("Found " + new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName() + " " + facing.getOpposite() + " from you."), true);

    }

}
