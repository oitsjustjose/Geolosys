package com.oitsjustjose.geolosys.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.config.ModConfig;
import com.oitsjustjose.geolosys.util.HelperFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRendererChunkBorder;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

public class ItemProPick extends Item
{
    private boolean showingChunkBorders;

    public ItemProPick()
    {
        this.showingChunkBorders = false;
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
    @SideOnly(Side.CLIENT)
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (!ModConfig.client.enableProPickExtras)
        {
            return;
        }
        if (isSelected && !showingChunkBorders)
        {
            showingChunkBorders = Minecraft.getMinecraft().debugRenderer.toggleChunkBorders();
        }
        else if (!isSelected && showingChunkBorders)
        {
            showingChunkBorders = Minecraft.getMinecraft().debugRenderer.toggleChunkBorders();
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

        if (pos.getY() >= worldIn.getTopSolidOrLiquidBlock(pos).getY() - 1)
        {
            String depositInChunk;
            try
            {
                depositInChunk = HelperFunctions.getTranslation("geolosys.pro_pick.tooltip.nonefound");
            }
            // If on a dedicated server, getTranslation will throw a NSME because it's SideOnly(CLIENT)
            catch (NoSuchMethodError onServerError)
            {
                depositInChunk = "No deposits in this area";
            }
            for (GeolosysAPI.ChunkPosSerializable chunkPos : GeolosysAPI.getCurrentWorldDeposits().keySet())
            {
                if (chunkPos.getX() == pos.getX() / 16)
                {
                    if (chunkPos.getZ() == pos.getZ() / 16)
                    {
                        if (chunkPos.getDimension() == worldIn.provider.getDimension())
                        {
                            String rawName = GeolosysAPI.getCurrentWorldDeposits().get(chunkPos);
                            try
                            {
                                depositInChunk = new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(rawName.split(":")[0], rawName.split(":")[1])), 1, Integer.parseInt(rawName.split(":")[2])).getDisplayName() + HelperFunctions.getTranslation("geolosys.pro_pick.tooltip.found");
                            }
                            catch (NullPointerException ignored)
                            {
                            }
                            // If on a dedicated server, getTranslation will throw a NSME because it's SideOnly(CLIENT)
                            catch (NoSuchMethodError onServerError)
                            {
                                depositInChunk = new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(rawName.split(":")[0], rawName.split(":")[1])), 1, Integer.parseInt(rawName.split(":")[2])).getDisplayName() + " found in this area";
                            }
                            break;
                        }
                    }
                }
            }
            player.sendStatusMessage(new TextComponentString(depositInChunk), true);
        }
        else
        {
            int xStart;
            int xEnd;
            int yStart;
            int yEnd;
            int zStart;
            int zEnd;
            int confAmt = ModConfig.featureControl.proPickRange;
            int confDmt = ModConfig.featureControl.proPickDiameter;

            boolean found = false;

            switch (facing)
            {
                case UP:
                    xStart = -(confDmt / 2);
                    xEnd = confDmt / 2;
                    yStart = -confAmt;
                    yEnd = 0;
                    zStart = -(confDmt / 2);
                    zEnd = (confDmt / 2);
                    found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                    break;
                case DOWN:
                    xStart = -(confDmt / 2);
                    xEnd = confDmt / 2;
                    yStart = 0;
                    yEnd = confAmt;
                    zStart = -(confDmt / 2);
                    zEnd = confDmt / 2;
                    found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                    break;
                case NORTH:
                    xStart = -(confDmt / 2);
                    xEnd = confDmt / 2;
                    yStart = -(confDmt / 2);
                    yEnd = confDmt / 2;
                    zStart = 0;
                    zEnd = confAmt;
                    found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                    break;
                case SOUTH:
                    xStart = -(confDmt / 2);
                    xEnd = confDmt / 2;
                    yStart = -(confDmt / 2);
                    yEnd = confDmt / 2;
                    zStart = -confAmt;
                    zEnd = 0;

                    found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                    break;
                case EAST:
                    xStart = -(confAmt);
                    xEnd = 0;
                    yStart = -(confDmt / 2);
                    yEnd = confDmt / 2;
                    zStart = -(confDmt / 2);
                    zEnd = confDmt / 2;

                    found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                    break;
                case WEST:
                    xStart = 0;
                    xEnd = confAmt;
                    yStart = -(confDmt / 2);
                    yEnd = confDmt / 2;
                    zStart = -(confDmt / 2);
                    zEnd = confDmt / 2;
                    found = isFound(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                    break;
            }
            if (!found)
            {
                player.sendStatusMessage(new TextComponentString("No deposits found"), true);
            }
        }
        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
    }

    private boolean isFound(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd)
    {
        boolean found = false;
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
        return found;
    }

    private void foundMessage(EntityPlayer player, IBlockState state, EnumFacing facing)
    {
        player.sendStatusMessage(new TextComponentString("Found " + new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName() + " " + facing.getOpposite() + " from you."), true);
    }
}
