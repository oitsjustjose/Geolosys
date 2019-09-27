package com.oitsjustjose.geolosys.common.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.api.world.DepositStone;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.config.ModConfig.SURFACE_PROSPECTING_TYPE;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.Utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ProPickItem extends Item
{
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Constants.MODID, "prospectors_pick");

    public ProPickItem()
    {
        super(new Item.Properties().maxStackSize(1));
        this.setRegistryName(REGISTRY_NAME);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        if (ModConfig.ENABLE_PRO_PICK_DMG.get())
        {
            if (stack.getTag() == null)
            {
                stack.setTag(new CompoundNBT());
                stack.getTag().putInt("damage", ModConfig.PRO_PICK_DURABILITY.get());
            }
            return 1D - (double) stack.getTag().getInt("damage") / (double) ModConfig.PRO_PICK_DURABILITY.get();
        }
        else
        {
            return 1;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
            ITooltipFlag flagIn)
    {
        if (ModConfig.ENABLE_PRO_PICK_DMG.get() && Minecraft.getInstance().gameSettings.advancedItemTooltips)
        {
            if (stack.getTag() == null || !stack.getTag().contains("damage"))
            {
                tooltip.add(new StringTextComponent("Durability: " + ModConfig.PRO_PICK_DURABILITY.get()));
            }
            else
            {
                tooltip.add(new StringTextComponent(
                        "Durability: " + stack.getTag().getInt("damage") + "/" + ModConfig.PRO_PICK_DURABILITY.get()));
            }
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return (ModConfig.ENABLE_PRO_PICK_DMG.get() && stack.hasTag());
    }

    public void attemptDamageItem(PlayerEntity player, BlockPos pos, Hand hand, World worldIn)
    {
        if (ModConfig.ENABLE_PRO_PICK_DMG.get() && !player.isCreative())
        {
            if (player.getHeldItem(hand).getItem() instanceof ProPickItem)
            {
                if (player.getHeldItem(hand).getTag() == null)
                {
                    player.getHeldItem(hand).setTag(new CompoundNBT());
                    player.getHeldItem(hand).getTag().putInt("damage", ModConfig.PRO_PICK_DURABILITY.get());
                }
                int prevDmg = player.getHeldItem(hand).getTag().getInt("damage");
                player.getHeldItem(hand).getTag().putInt("damage", (prevDmg - 1));
                if (player.getHeldItem(hand).getTag().getInt("damage") <= 0)
                {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                    worldIn.playSound(player, pos, new SoundEvent(new ResourceLocation("entity.item.break")),
                            SoundCategory.PLAYERS, 1.0F, 0.85F);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        if (player.isSneaking())
        {
            ItemStack stack = player.getHeldItem(hand);
            // If there's no stack compound make one and assume last state was ores
            if (stack.getTag() == null)
            {
                stack.setTag(new CompoundNBT());
                stack.getTag().putBoolean("stone", true);
            }
            // Swap boolean for compound state
            else
            {
                stack.getTag().putBoolean("stone", !stack.getTag().getBoolean("stone"));
            }

            boolean searchForStone = stack.getTag().getBoolean("stone");

            if (searchForStone)
            {
                player.sendStatusMessage(new TranslationTextComponent("geolosys.pro_pick.tooltip.mode.stones"), true);
            }
            else
            {
                player.sendStatusMessage(new TranslationTextComponent("geolosys.pro_pick.tooltip.mode.ores"), true);
            }
        }
        return new ActionResult<ItemStack>(ActionResultType.PASS, player.getHeldItem(hand));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        Direction facing = context.getFace();

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
                return ActionResultType.PASS;
            }

            ItemStack stack = player.getHeldItem(hand);

            // If there's no stack compound make one and assume last state was ores
            if (stack.getTag() == null)
            {
                stack.setTag(new CompoundNBT());
                stack.getTag().putBoolean("stone", false);
            }

            boolean searchForStone = stack.getTag().getBoolean("stone");

            int seaLvl = worldIn.getSeaLevel();

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
                int confAmt = ModConfig.PRO_PICK_RANGE.get();
                int confDmt = ModConfig.PRO_PICK_DIAMETER.get();

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
        return ActionResultType.SUCCESS;

    }

    private boolean prospectUnderground(PlayerEntity player, World worldIn, BlockPos pos, Direction facing, int xStart,
            int xEnd, int yStart, int yEnd, int zStart, int zEnd)
    {
        HashMap<IDeposit, ArrayList<BlockState>> foundMap = new HashMap<>();
        for (IDeposit ore : GeolosysAPI.plutonRegistry.getOres())
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
                    BlockState state = worldIn.getBlockState(pos.add(x, y, z));
                    for (IDeposit ore : GeolosysAPI.plutonRegistry.getOres())
                    {
                        // Use the Use the foundMap with multIDeposits
                        if (ore instanceof DepositMultiOre)
                        {
                            for (BlockState tmpState : ((DepositMultiOre) ore).oreBlocks.keySet())
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
                    for (BlockState state2 : GeolosysAPI.proPickExtras)
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
    @OnlyIn(Dist.CLIENT)
    public void onDrawScreen(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL
                || Minecraft.getInstance().debugRenderer.shouldRender()
                || Minecraft.getInstance().gameSettings.showDebugInfo
                || Minecraft.getInstance().gameSettings.showDebugProfilerChart)
        {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getHeldItemMainhand().getItem() instanceof ProPickItem
                || mc.player.getHeldItemOffhand().getItem() instanceof ProPickItem)
        {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();
            int seaLvl;
            try
            {
                seaLvl = this.dimensionSeaLevels.get(mc.player.getEntityWorld().getDimension());
            }
            catch (NullPointerException e)
            {
                seaLvl = mc.player.getEntityWorld().getSeaLevel();
            }
            int level = (int) (seaLvl - mc.player.posY);
            if (level < 0)
            {
                mc.fontRenderer.drawStringWithShadow("Depth: " + Math.abs(level) + "m above sea-level",
                        ModConfig.PRO_PICK_HUD_X.get(), ModConfig.PRO_PICK_HUD_Y.get(), 0xFFFFFFFF);
            }
            else if (level == 0)
            {
                mc.fontRenderer.drawStringWithShadow("Depth: at sea-level", ModConfig.PRO_PICK_HUD_X.get(),
                        ModConfig.PRO_PICK_HUD_Y.get(), 0xFFFFFFFF);
            }
            else
            {
                mc.fontRenderer.drawStringWithShadow("Depth: " + level + "m below sea-level",
                        ModConfig.PRO_PICK_HUD_X.get(), ModConfig.PRO_PICK_HUD_Y.get(), 0xFFFFFFFF);
            }
            GlStateManager.color4f(1F, 1F, 1F, 1F);
        }
    }

    private boolean prospectSurfaceOres(World world, BlockPos pos, PlayerEntity player)
    {
        ChunkPos tempPos = new ChunkPos(pos);

        SURFACE_PROSPECTING_TYPE searchType = ModConfig.prospecting.surfaceProspectingResults;

        HashMap<IDeposit, ArrayList<BlockState>> foundMap = new HashMap<>();
        for (IDeposit ore : GeolosysAPI.oreBlocks)
        {
            if (ore instanceof DepositMultIDeposit)
            {
                foundMap.put((DepositMultIDeposit) ore, new ArrayList<>());
            }
        }

        for (int x = tempPos.getXStart(); x <= tempPos.getXEnd(); x++)
        {
            for (int z = tempPos.getZStart(); z <= tempPos.getZEnd(); z++)
            {
                for (int y = 0; y < world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY(); y++)
                {
                    BlockState state = world.getBlockState(new BlockPos(x, y, z));

                    for (IDeposit ore : GeolosysAPI.oreBlocks)
                    {
                        if (ore instanceof DepositMultIDeposit)
                        {
                            DepositMultIDeposit multIDeposit = (DepositMultIDeposit) ore;
                            for (BlockState multIDepositState : (searchType == SURFACE_PROSPECTING_TYPE.OREBLOCKS
                                    ? multIDeposit.oreBlocks.keySet()
                                    : multIDeposit.sampleBlocks.keySet()))
                            {
                                if (Utils.doStatesMatch(state, multIDepositState))
                                {
                                    foundMap.get(ore).add(state);
                                    if (foundMap.get(ore).size() == multIDeposit.oreBlocks.keySet().size())
                                    {
                                        Geolosys.proxy.sendProspectingMessage(player,
                                                multIDeposit.getFriendlyName(world, pos, player), null);
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
                    for (BlockState state2 : GeolosysAPI.proPickExtras)
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

        player.sendStatusMessage(new TranslationTextComponent("geolosys.pro_pick.tooltip.nonefound_surface"), true);
        return false;
    }

    private boolean prospectSurfaceStones(World world, BlockPos pos, PlayerEntity player)
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
        player.sendStatusMessage(new TranslationTextComponent("geolosys.pro_pick.tooltip.nonefound_stone_surface"),
                true);
        return false;

    }
}