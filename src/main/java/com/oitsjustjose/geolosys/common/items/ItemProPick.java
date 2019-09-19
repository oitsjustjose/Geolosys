package com.oitsjustjose.geolosys.common.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.common.api.world.DepositStone;
import com.oitsjustjose.geolosys.common.api.world.IOre;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.config.ModConfig.Prospecting.SURFACE_PROSPECTING_TYPE;
import com.oitsjustjose.geolosys.common.util.Utils;

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
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProPick extends Item
{
    private HashMap<Integer, Integer> dimensionSeaLevels;

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

    /**
     * @param dimensionSeaLevels the dimensionSeaLevels to set
     */
    public void setDimensionSeaLevels(HashMap<Integer, Integer> dimensionSeaLevels)
    {
        this.dimensionSeaLevels = dimensionSeaLevels;
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
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (player.isSneaking())
        {
            ItemStack stack = player.getHeldItem(hand);
            // If there's no stack compound make one and assume last state was ores
            if (stack.getTagCompound() == null)
            {
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setBoolean("stone", true);
            }
            // Swap boolean for compound state
            else
            {
                stack.getTagCompound().setBoolean("stone", !stack.getTagCompound().getBoolean("stone"));
            }

            boolean searchForStone = stack.getTagCompound().getBoolean("stone");

            if (searchForStone)
            {
                player.sendStatusMessage(new TextComponentTranslation("geolosys.pro_pick.tooltip.mode.stones"), true);
            }
            else
            {
                player.sendStatusMessage(new TextComponentTranslation("geolosys.pro_pick.tooltip.mode.ores"), true);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
            EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (player.isSneaking())
        {
            this.onItemRightClick(worldIn, player, hand);
        }
        else
        {
            this.attemptDamageItem(player, pos, hand, worldIn);
            // At surface or higher
            if (worldIn.isRemote)
            {
                player.swingArm(hand);
                return EnumActionResult.PASS;
            }

            ItemStack stack = player.getHeldItem(hand);

            // If there's no stack compound make one and assume last state was ores
            if (stack.getTagCompound() == null)
            {
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setBoolean("stone", false);
            }

            boolean searchForStone = stack.getTagCompound().getBoolean("stone");

            int seaLvl;
            try
            {
                seaLvl = this.dimensionSeaLevels.get(player.dimension);
            }
            catch (NullPointerException e)
            {
                seaLvl = worldIn.getSeaLevel();
            }

            if (player.getPosition().getY() >= seaLvl)
            {
                if (searchForStone)
                {
                    prospectSurfaceStones(worldIn, pos, player);
                }
                else
                {
                    prospectSurfaceOres(worldIn, pos, player);
                }
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

                boolean oreFoundUnderground = false;

                switch (facing)
                {
                case UP:
                    xStart = -(confDmt / 2);
                    xEnd = confDmt / 2;
                    yStart = -confAmt;
                    yEnd = 0;
                    zStart = -(confDmt / 2);
                    zEnd = (confDmt / 2);
                    oreFoundUnderground = prospectUnderground(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd,
                            zStart, zEnd);
                    break;
                case DOWN:
                    xStart = -(confDmt / 2);
                    xEnd = confDmt / 2;
                    yStart = 0;
                    yEnd = confAmt;
                    zStart = -(confDmt / 2);
                    zEnd = confDmt / 2;
                    oreFoundUnderground = prospectUnderground(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd,
                            zStart, zEnd);
                    break;
                case NORTH:
                    xStart = -(confDmt / 2);
                    xEnd = confDmt / 2;
                    yStart = -(confDmt / 2);
                    yEnd = confDmt / 2;
                    zStart = 0;
                    zEnd = confAmt;
                    oreFoundUnderground = prospectUnderground(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd,
                            zStart, zEnd);
                    break;
                case SOUTH:
                    xStart = -(confDmt / 2);
                    xEnd = confDmt / 2;
                    yStart = -(confDmt / 2);
                    yEnd = confDmt / 2;
                    zStart = -confAmt;
                    zEnd = 0;
                    oreFoundUnderground = prospectUnderground(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd,
                            zStart, zEnd);
                    break;
                case EAST:
                    xStart = -(confAmt);
                    xEnd = 0;
                    yStart = -(confDmt / 2);
                    yEnd = confDmt / 2;
                    zStart = -(confDmt / 2);
                    zEnd = confDmt / 2;
                    oreFoundUnderground = prospectUnderground(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd,
                            zStart, zEnd);
                    break;
                case WEST:
                    xStart = 0;
                    xEnd = confAmt;
                    yStart = -(confDmt / 2);
                    yEnd = confDmt / 2;
                    zStart = -(confDmt / 2);
                    zEnd = confDmt / 2;
                    oreFoundUnderground = prospectUnderground(player, worldIn, pos, facing, xStart, xEnd, yStart, yEnd,
                            zStart, zEnd);
                    break;
                }
                // If right clicking yielded nothing, then find the ore in the chunk again
                if (!oreFoundUnderground)
                {
                    prospectSurfaceOres(worldIn, pos, player);
                }
            }
            player.swingArm(hand);
        }
        return EnumActionResult.SUCCESS;
    }

    private boolean prospectUnderground(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing facing, int xStart,
            int xEnd, int yStart, int yEnd, int zStart, int zEnd)
    {
        HashMap<IOre, ArrayList<IBlockState>> foundMap = new HashMap<>();
        for (IOre ore : GeolosysAPI.oreBlocks)
        {
            if (ore instanceof DepositMultiOre)
            {
                foundMap.put((DepositMultiOre) ore, new ArrayList<>());
            }
        }

        for (int x = xStart; x <= xEnd; x++)
        {
            for (int y = yStart; y <= yEnd; y++)
            {
                for (int z = zStart; z <= zEnd; z++)
                {
                    IBlockState state = worldIn.getBlockState(pos.add(x, y, z));
                    for (IOre ore : GeolosysAPI.oreBlocks)
                    {
                        // Use the Use the foundMap with multiOres
                        if (ore instanceof DepositMultiOre)
                        {
                            for (IBlockState tmpState : ((DepositMultiOre) ore).oreBlocks.keySet())
                            {
                                if (Utils.doStatesMatch(tmpState, state))
                                {
                                    foundMap.get(ore).add(state);
                                    if (foundMap.get(ore).size() == ((DepositMultiOre) ore).oreBlocks.keySet().size())
                                    {
                                        Geolosys.proxy.sendProspectingMessage(player,
                                                ((DepositMultiOre) ore).getFriendlyName(worldIn, pos, player),
                                                facing.getOpposite());
                                        return true;
                                    }
                                }
                            }
                        }
                        // Just check the ore itself otherwise
                        else if (Utils.doStatesMatch(ore.getOre(), state))
                        {
                            Geolosys.proxy.sendProspectingMessage(player, Utils.blockStateToStack(ore.getOre()),
                                    facing.getOpposite());
                            return true;
                        }
                    }
                    // If we didn't find anything yet
                    for (IBlockState state2 : GeolosysAPI.proPickExtras)
                    {
                        if (Utils.doStatesMatch(state2, state))
                        {
                            Geolosys.proxy.sendProspectingMessage(player, Utils.blockStateToStack(state),
                                    facing.getOpposite());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDrawScreen(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL
                || Minecraft.getMinecraft().debugRenderer.shouldRender()
                || Minecraft.getMinecraft().gameSettings.showDebugInfo
                || Minecraft.getMinecraft().gameSettings.showDebugProfilerChart)
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
            int seaLvl;
            try
            {
                seaLvl = this.dimensionSeaLevels.get(mc.player.dimension);
            }
            catch (NullPointerException e)
            {
                seaLvl = mc.player.getEntityWorld().getSeaLevel();
            }
            int level = (int) (seaLvl - mc.player.posY);
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

    private boolean prospectSurfaceOres(World world, BlockPos pos, EntityPlayer player)
    {
        ChunkPos tempPos = new ChunkPos(pos);

        SURFACE_PROSPECTING_TYPE searchType = ModConfig.prospecting.surfaceProspectingResults;

        HashMap<IOre, ArrayList<IBlockState>> foundMap = new HashMap<>();
        for (IOre ore : GeolosysAPI.oreBlocks)
        {
            if (ore instanceof DepositMultiOre)
            {
                foundMap.put((DepositMultiOre) ore, new ArrayList<>());
            }
        }

        for (int x = tempPos.getXStart(); x <= tempPos.getXEnd(); x++)
        {
            for (int z = tempPos.getZStart(); z <= tempPos.getZEnd(); z++)
            {
                for (int y = 0; y < world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(); y++)
                {
                    IBlockState state = world.getBlockState(new BlockPos(x, y, z));

                    for (IOre ore : GeolosysAPI.oreBlocks)
                    {
                        if (ore instanceof DepositMultiOre)
                        {
                            DepositMultiOre multiOre = (DepositMultiOre) ore;
                            for (IBlockState multiOreState : (searchType == SURFACE_PROSPECTING_TYPE.OREBLOCKS
                                    ? multiOre.oreBlocks.keySet()
                                    : multiOre.sampleBlocks.keySet()))
                            {
                                if (Utils.doStatesMatch(state, multiOreState))
                                {
                                    foundMap.get(ore).add(state);
                                    if (foundMap.get(ore).size() == multiOre.oreBlocks.keySet().size())
                                    {
                                        Geolosys.proxy.sendProspectingMessage(player,
                                                multiOre.getFriendlyName(world, pos, player), null);
                                        return true;
                                    }
                                }
                            }
                        }
                        else
                        {
                            if (Utils.doStatesMatch(state,
                                    (searchType == SURFACE_PROSPECTING_TYPE.OREBLOCKS ? ore.getOre()
                                            : ore.getSample())))
                            {
                                Geolosys.proxy.sendProspectingMessage(player, Utils.blockStateToStack(ore.getOre()),
                                        null);
                                return true;
                            }
                        }
                    }
                    for (IBlockState state2 : GeolosysAPI.proPickExtras)
                    {
                        if (Utils.doStatesMatch(state, state2))
                        {
                            Geolosys.proxy.sendProspectingMessage(player, Utils.blockStateToStack(state), null);
                            return true;
                        }
                    }
                }
            }
        }

        player.sendStatusMessage(new TextComponentTranslation("geolosys.pro_pick.tooltip.nonefound_surface"), true);
        return false;
    }

    private boolean prospectSurfaceStones(World world, BlockPos pos, EntityPlayer player)
    {
        ChunkPos tempPos = new ChunkPos(pos);

        for (int x = tempPos.getXStart(); x <= tempPos.getXEnd(); x++)
        {
            for (int z = tempPos.getZStart(); z <= tempPos.getZEnd(); z++)
            {
                for (int y = 0; y < world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(); y++)
                {
                    for (DepositStone s : GeolosysAPI.stones)
                    {
                        if (Utils.doStatesMatch(s.getOre(), world.getBlockState(new BlockPos(x, y, z))))
                        {
                            Geolosys.proxy.sendProspectingMessage(player,
                                    Utils.blockStateToStack(world.getBlockState(new BlockPos(x, y, z))), null);
                            return true;
                        }
                    }
                }
            }
        }
        player.sendStatusMessage(new TextComponentTranslation("geolosys.pro_pick.tooltip.nonefound_stone_surface"),
                true);
        return false;

    }
}