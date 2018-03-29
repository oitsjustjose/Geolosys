package com.oitsjustjose.geolosys.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.config.ModConfig;
import com.oitsjustjose.geolosys.util.HelperFunctions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class ItemProPick extends Item
{
    public ItemProPick()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setRegistryName(new ResourceLocation(Geolosys.MODID, "PRO_PICK"));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        ForgeRegistries.ITEMS.register(this);
        this.registerModel();
    }

    private void registerModel()
    {
        Geolosys.getInstance().clientRegistry.register(new ItemStack(this), new ResourceLocation(this.getRegistryName().toString()), "inventory");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return stack.getItem().getRegistryName().toString().replaceAll(":", ".");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            tooltip.add(HelperFunctions.getTranslation(this.getUnlocalizedName(stack) + ".tooltip"));
        }
        else
        {
            tooltip.add(HelperFunctions.getTranslation("shift.tooltip"));
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            player.swingArm(hand);
            return EnumActionResult.PASS;
        }

        int xStart;
        int xEnd;
        int yStart;
        int yEnd;
        int zStart;
        int zEnd;
        int confAmt = ModConfig.featureControl.proPickRange;

        boolean found = false;

        switch (facing)
        {
            case UP:
                xStart = -(confAmt / 2);
                xEnd = confAmt / 2;
                yStart = -confAmt;
                yEnd = 0;
                zStart = -(confAmt / 2);
                zEnd = (confAmt / 2);
                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (GeolosysAPI.oreBlocks.keySet().contains(state))
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
                xStart = -(confAmt / 2);
                xEnd = confAmt / 2;
                yStart = 0;
                yEnd = confAmt;
                zStart = -(confAmt / 2);
                zEnd = confAmt / 2;
                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (GeolosysAPI.oreBlocks.keySet().contains(state))
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
                xStart = -(confAmt / 2);
                xEnd = confAmt / 2;
                yStart = -(confAmt / 2);
                yEnd = confAmt / 2;
                zStart = 0;
                zEnd = confAmt;
                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (GeolosysAPI.oreBlocks.keySet().contains(state))
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
                xStart = -(confAmt / 2);
                xEnd = confAmt / 2;
                yStart = -(confAmt / 2);
                yEnd = confAmt / 2;
                zStart = -confAmt;
                zEnd = 0;

                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (GeolosysAPI.oreBlocks.keySet().contains(state))
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
                xStart = -(confAmt);
                xEnd = 0;
                yStart = -(confAmt / 2);
                yEnd = confAmt / 2;
                zStart = -(confAmt / 2);
                zEnd = confAmt / 2;

                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (GeolosysAPI.oreBlocks.keySet().contains(state))
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
                xEnd = confAmt;
                yStart = -(confAmt / 2);
                yEnd = confAmt / 2;
                zStart = -(confAmt / 2);
                zEnd = confAmt / 2;
                for (int x = xStart; x <= xEnd; x++)
                {
                    for (int y = yStart; y <= yEnd; y++)
                    {
                        for (int z = zStart; z <= zEnd; z++)
                        {
                            IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                            if (GeolosysAPI.oreBlocks.keySet().contains(state))
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

    private void foundMessage(EntityPlayer player, IBlockState state, EnumFacing facing)
    {
        player.sendStatusMessage(new TextComponentString("Found " + new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName() + " " + facing.getOpposite() + " from you."), true);
    }
}
