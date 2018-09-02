package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

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
        Geolosys.getInstance().clientRegistry.register(new ItemStack(this), new ResourceLocation(Objects.requireNonNull(this.getRegistryName()).toString()), "inventory");
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
            return 1D - (double) stack.getTagCompound().getInteger("damage") / (double) ModConfig.prospecting.proPickDurability;
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
                tooltip.add("Durability: " + stack.getTagCompound().getInteger("damage") + "/" + ModConfig.prospecting.proPickDurability);
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
                    player.getHeldItem(hand).getTagCompound().setInteger("damage", ModConfig.prospecting.proPickDurability);
                }
                int prevDmg = player.getHeldItem(hand).getTagCompound().getInteger("damage");
                player.getHeldItem(hand).getTagCompound().setInteger("damage", (prevDmg - 1));
                if (player.getHeldItem(hand).getTagCompound().getInteger("damage") <= 0)
                {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                    worldIn.playSound(player, pos, new SoundEvent(new ResourceLocation("entity.item.break")), SoundCategory.PLAYERS, 1.0F, 0.85F);
                }
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
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
            String depositInChunk = I18n.translateToLocal("geolosys.pro_pick.tooltip.nonefound_surface");
            ChunkPos tempPos = new ChunkPos(pos);

            if (!ModConfig.featureControl.shouldTrackOres || GeolosysAPI.mineralMap.get(tempPos) != null)
            {
                for (GeolosysAPI.ChunkPosSerializable chunkPos : GeolosysAPI.getCurrentWorldDeposits().keySet())
                {
                    if (chunkPos.getX() == tempPos.x)
                    {
                        if (chunkPos.getZ() == tempPos.z)
                        {
                            if (chunkPos.getDimension() == worldIn.provider.getDimension())
                            {
                                String rawName = GeolosysAPI.getCurrentWorldDeposits().get(chunkPos);
                                depositInChunk = new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(rawName.split(":")[0], rawName.split(":")[1]))), 1, Integer.parseInt(rawName.split(":")[2])).getDisplayName() + " " + I18n.translateToLocal("geolosys.pro_pick.tooltip.found");
                                break;
                            }
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
                player.sendStatusMessage(new TextComponentString(I18n.translateToLocal("geolosys.pro_pick.tooltip.nonefound")), true);
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
        player.sendStatusMessage(new TextComponentString("Found " + new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName() + " " + facing.getOpposite() + " from you."), true);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDrawScreen(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || Minecraft.getMinecraft().debugRenderer.shouldRender())
        {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemProPick || mc.player.getHeldItemOffhand().getItem() instanceof ItemProPick)
        {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();
            int level = (int) (mc.player.world.getSeaLevel() - mc.player.posY);
            if (level < 0)
            {
                mc.fontRenderer.drawStringWithShadow("Depth: " + Math.abs(level) + "m above sea-level", ModConfig.client.hudX, ModConfig.client.hudY, 0xFFFFFFFF);
            }
            else if (level == 0)
            {
                mc.fontRenderer.drawStringWithShadow("Depth: at sea-level", ModConfig.client.hudX, ModConfig.client.hudY, 0xFFFFFFFF);
            }
            else
            {
                mc.fontRenderer.drawStringWithShadow("Depth: " + level + "m below sea-level", ModConfig.client.hudX, ModConfig.client.hudY, 0xFFFFFFFF);
            }
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }
}
