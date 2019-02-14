package com.oitsjustjose.geolosys.common.items;

import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.config.ModConfig.Prospecting.SURFACE_PROSPECTING_TYPE;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProPick extends Item
{
    public ItemProPick()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setRegistryName(new ResourceLocation(Geolosys.MODID, "PRO_PICK"));
        this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString().replaceAll(":", "."));
        MinecraftForge.EVENT_BUS.register(this);
        ForgeRegistries.ITEMS.register(this);
        this.registerModel();
    }

    private void registerModel()
    {
        Geolosys.getInstance().clientRegistry.register(new ItemStack(this),
                new ResourceLocation(Objects.requireNonNull(this.getRegistryName()).toString()), "inventory");
    }

    @Override
    public String getUnlocalizedName(@Nonnull ItemStack stack)
    {
        return Objects.requireNonNull(stack.getItem().getRegistryName()).toString().replaceAll(":", ".");
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        if (ModConfig.prospecting.enableProPickDamage)
        {
            if (stack.getTagCompound() == null)
            {
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setInteger("damage", ModConfig.prospecting.proPickDurability);
            }
            return 1D - (double) stack.getTagCompound().getInteger("damage")
                    / (double) ModConfig.prospecting.proPickDurability;
        }
        else
        {
            return 1;
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (ModConfig.prospecting.enableProPickDamage && Minecraft.getMinecraft().gameSettings.advancedItemTooltips)
        {
            if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("damage"))
            {
                tooltip.add("Durability: " + ModConfig.prospecting.proPickDurability);
            }
            else
            {
                tooltip.add("Durability: " + stack.getTagCompound().getInteger("damage") + "/"
                        + ModConfig.prospecting.proPickDurability);
            }
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return (ModConfig.prospecting.enableProPickDamage && stack.hasTagCompound());
    }

    public void attemptDamageItem(EntityPlayer player, BlockPos pos, EnumHand hand, World worldIn)
    {
        if (ModConfig.prospecting.enableProPickDamage && !player.capabilities.isCreativeMode)
        {
            if (player.getHeldItem(hand).getItem() instanceof ItemProPick)
            {
                if (player.getHeldItem(hand).getTagCompound() == null)
                {
                    player.getHeldItem(hand).setTagCompound(new NBTTagCompound());
                    player.getHeldItem(hand).getTagCompound().setInteger("damage",
                            ModConfig.prospecting.proPickDurability);
                }
                int prevDmg = player.getHeldItem(hand).getTagCompound().getInteger("damage");
                player.getHeldItem(hand).getTagCompound().setInteger("damage", (prevDmg - 1));
                if (player.getHeldItem(hand).getTagCompound().getInteger("damage") <= 0)
                {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                    worldIn.playSound(player, pos, new SoundEvent(new ResourceLocation("entity.item.break")),
                            SoundCategory.PLAYERS, 1.0F, 0.85F);
                }
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
            EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        this.attemptDamageItem(player, pos, hand, worldIn);
        // At surface or higher
        if (worldIn.isRemote)
        {
            player.swingArm(hand);
            return EnumActionResult.PASS;
        }
        if (player.getPosition().getY() >= player.world.getSeaLevel())
        {
            player.sendStatusMessage(new TextComponentString(findOreInChunk(worldIn, pos)), true);
        }
        else
        {
            int xStart;
            int xEnd;
            int yStart;
            int yEnd;
            int zStart;
            int zEnd;
            int confAmt = ModConfig.prospecting.proPickRange;
            int confDmt = ModConfig.prospecting.proPickDiameter;

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
                player.sendStatusMessage(new TextComponentString(findOreInChunk(worldIn, pos)), true);
            }
        }
        player.swingArm(hand);
        return EnumActionResult.SUCCESS;
    }

    private boolean isFound(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, int xStart, int xEnd,
            int yStart, int yEnd, int zStart, int zEnd)
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
                        sendFoundMessage(player, state, facing);
                        found = true;
                        break;
                    }
                }
            }
        }
        return found;
    }

    private void sendFoundMessage(EntityPlayer player, IBlockState state, EnumFacing facing)
    {
        String msg = Geolosys.proxy.translate("geolosys.pro_pick.tooltip.found");
        msg = msg.replace("%BLOCK%",
                new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName());
        msg = msg.replace("%DIR%", "" + facing.getOpposite());
        player.sendStatusMessage(new TextComponentString(msg), true);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDrawScreen(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL
                || Minecraft.getMinecraft().debugRenderer.shouldRender())
        {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemProPick
                || mc.player.getHeldItemOffhand().getItem() instanceof ItemProPick)
        {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();
            int level = (int) (mc.player.world.getSeaLevel() - mc.player.posY);
            if (level < 0)
            {
                mc.fontRenderer.drawStringWithShadow("Depth: " + Math.abs(level) + "m above sea-level",
                        ModConfig.client.hudX, ModConfig.client.hudY, 0xFFFFFFFF);
            }
            else if (level == 0)
            {
                mc.fontRenderer.drawStringWithShadow("Depth: at sea-level", ModConfig.client.hudX,
                        ModConfig.client.hudY, 0xFFFFFFFF);
            }
            else
            {
                mc.fontRenderer.drawStringWithShadow("Depth: " + level + "m below sea-level", ModConfig.client.hudX,
                        ModConfig.client.hudY, 0xFFFFFFFF);
            }
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }

    private String findOreInChunk(World world, BlockPos pos)
    {
        ChunkPos tempPos = new ChunkPos(pos);

        SURFACE_PROSPECTING_TYPE searchType = ModConfig.prospecting.surfaceProspectingResults;

        if (searchType == SURFACE_PROSPECTING_TYPE.OREBLOCKS)
        {
            for (int x = tempPos.getXStart(); x <= tempPos.getXEnd(); x++)
            {
                for (int z = tempPos.getZStart(); z <= tempPos.getZEnd(); z++)
                {
                    for (int y = 0; y < world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(); y++)
                    {
                        if (GeolosysAPI.oreBlocks.keySet().contains(world.getBlockState(new BlockPos(x, y, z))))
                        {
                            return getNameForBlockState(world.getBlockState(new BlockPos(x, y, z))) + " "
                                    + Geolosys.proxy.translate("geolosys.pro_pick.tooltip.found_surface");
                        }
                    }
                }
            }
        }
        else
        {
            for (int x = tempPos.getXStart(); x <= tempPos.getXEnd(); x++)
            {
                for (int z = tempPos.getZStart(); z <= tempPos.getZEnd(); z++)
                {
                    for (int y = 0; y < world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(); y++)
                    {
                        for (Entry<IBlockState, IBlockState> e : GeolosysAPI.oreBlocks.entrySet())
                        {
                            if (e.getValue() == world.getBlockState(new BlockPos(x, y, z)))
                            {
                                return getNameForBlockState(e.getKey()) + " "
                                        + Geolosys.proxy.translate("geolosys.pro_pick.tooltip.found_surface");
                            }
                        }
                    }
                }
            }
        }

        return Geolosys.proxy.translate("geolosys.pro_pick.tooltip.nonefound_surface");
    }

    private String getNameForBlockState(IBlockState state)
    {
        return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName();
    }
}
