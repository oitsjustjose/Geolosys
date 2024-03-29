package com.oitsjustjose.geolosys.common.items;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import com.oitsjustjose.geolosys.common.utils.Prospecting;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
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

public class ProPickItem extends Item {
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Constants.MODID, "prospectors_pick");

    public ProPickItem() {
        super(new Item.Properties().maxStackSize(1).group(GeolosysGroup.getInstance()));
        this.setRegistryName(REGISTRY_NAME);
        Geolosys.proxy.registerClientSubscribeEvent(this);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (CommonConfig.ENABLE_PRO_PICK_DMG.get()) {
            if (stack.getTag() == null) {
                stack.setTag(new CompoundNBT());
                stack.getTag().putInt("damage", CommonConfig.PRO_PICK_DURABILITY.get());
            }
            return 1D - (double) stack.getTag().getInt("damage") / (double) CommonConfig.PRO_PICK_DURABILITY.get();
        } else {
            return 1;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
            ITooltipFlag flagIn) {
        if (CommonConfig.ENABLE_PRO_PICK_DMG.get() && Minecraft.getInstance().gameSettings.advancedItemTooltips) {
            if (stack.getTag() == null || !stack.getTag().contains("damage")) {
                tooltip.add(new StringTextComponent("Durability: " + CommonConfig.PRO_PICK_DURABILITY.get()));
            } else {
                tooltip.add(new StringTextComponent("Durability: " + stack.getTag().getInt("damage") + "/"
                        + CommonConfig.PRO_PICK_DURABILITY.get()));
            }
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return (CommonConfig.ENABLE_PRO_PICK_DMG.get() && stack.hasTag());
    }

    public void attemptDamageItem(PlayerEntity player, BlockPos pos, Hand hand, World worldIn) {
        if (CommonConfig.ENABLE_PRO_PICK_DMG.get() && !player.isCreative()) {
            if (player.getHeldItem(hand).getItem() instanceof ProPickItem) {
                if (player.getHeldItem(hand).getTag() == null) {
                    player.getHeldItem(hand).setTag(new CompoundNBT());
                    player.getHeldItem(hand).getTag().putInt("damage", CommonConfig.PRO_PICK_DURABILITY.get());
                }
                int prevDmg = player.getHeldItem(hand).getTag().getInt("damage");
                player.getHeldItem(hand).getTag().putInt("damage", (prevDmg - 1));
                if (player.getHeldItem(hand).getTag().getInt("damage") <= 0) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                    worldIn.playSound(player, pos, new SoundEvent(new ResourceLocation("entity.item.break")),
                            SoundCategory.PLAYERS, 1.0F, 0.85F);
                }
            }
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        Direction facing = context.getFace();

        if (player.isCrouching()) {
            this.onItemRightClick(worldIn, player, hand);
        } else {
            if (!player.isCreative()) {
                this.attemptDamageItem(player, pos, hand, worldIn);
            }
            // At surface or higher
            if (worldIn.isRemote) {
                player.swingArm(hand);
                return ActionResultType.PASS;
            }

            ItemStack stack = player.getHeldItem(hand);

            int xStart;
            int xEnd;
            int yStart;
            int yEnd;
            int zStart;
            int zEnd;
            int confAmt = CommonConfig.PRO_PICK_RANGE.get();
            int confDmt = CommonConfig.PRO_PICK_DIAMETER.get();

            switch (facing) {
            case UP:
                xStart = -(confDmt / 2);
                xEnd = confDmt / 2;
                yStart = -confAmt;
                yEnd = 0;
                zStart = -(confDmt / 2);
                zEnd = (confDmt / 2);
                prospect(player, stack, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                break;
            case DOWN:
                xStart = -(confDmt / 2);
                xEnd = confDmt / 2;
                yStart = 0;
                yEnd = confAmt;
                zStart = -(confDmt / 2);
                zEnd = confDmt / 2;
                prospect(player, stack, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                break;
            case NORTH:
                xStart = -(confDmt / 2);
                xEnd = confDmt / 2;
                yStart = -(confDmt / 2);
                yEnd = confDmt / 2;
                zStart = 0;
                zEnd = confAmt;
                prospect(player, stack, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                break;
            case SOUTH:
                xStart = -(confDmt / 2);
                xEnd = confDmt / 2;
                yStart = -(confDmt / 2);
                yEnd = confDmt / 2;
                zStart = -confAmt;
                zEnd = 0;
                prospect(player, stack, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                break;
            case EAST:
                xStart = -(confAmt);
                xEnd = 0;
                yStart = -(confDmt / 2);
                yEnd = confDmt / 2;
                zStart = -(confDmt / 2);
                zEnd = confDmt / 2;
                prospect(player, stack, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                break;
            case WEST:
                xStart = 0;
                xEnd = confAmt;
                yStart = -(confDmt / 2);
                yEnd = confDmt / 2;
                zStart = -(confDmt / 2);
                zEnd = confDmt / 2;
                prospect(player, stack, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
                break;
            }

            player.swingArm(hand);
        }
        return ActionResultType.SUCCESS;
    }

    private boolean prospect(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction facing,
            int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd) {

        HashSet<BlockState> foundBlocks = new HashSet<BlockState>();
        HashSet<BlockPos> foundBlockPos = new HashSet<BlockPos>();
        HashSet<BlockState> depositBlocks = Prospecting.getDepositBlocks();

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                for (int z = zStart; z <= zEnd; z++) {
                    BlockPos tmpPos = pos.add(x, y, z);
                    BlockState state = world.getBlockState(tmpPos);
                    if (depositBlocks.contains(state)) {
                        foundBlocks.add(state);
                        foundBlockPos.add(tmpPos);
                    }
                }
            }
        }

        if (!foundBlocks.isEmpty()) {
            Geolosys.proxy.sendProspectingMessage(player, foundBlocks, facing.getOpposite());
            foundBlockPos.forEach((_pos) -> {
                world.playSound(null, _pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.15F, 2F);
            });
            return true;
        }
        return prospectChunk(world, stack, pos, player);
    }

    private boolean prospectChunk(World world, ItemStack stack, BlockPos pos, PlayerEntity player) {
        HashSet<BlockState> foundBlocks = new HashSet<BlockState>();
        HashSet<BlockState> depositBlocks = Prospecting.getDepositBlocks();
        ChunkPos tempPos = new ChunkPos(pos);

        for (int x = tempPos.getXStart(); x <= tempPos.getXEnd(); x++) {
            for (int z = tempPos.getZStart(); z <= tempPos.getZEnd(); z++) {
                for (int y = 0; y < world.getHeight(); y++) {
                    BlockState state = world.getBlockState(new BlockPos(x, y, z));
                    if (depositBlocks.contains(state)) {
                        foundBlocks.add(state);
                    }
                }
            }
        }

        if (!foundBlocks.isEmpty()) {
            Geolosys.proxy.sendProspectingMessage(player, foundBlocks, null);
            return true;
        }

        player.sendStatusMessage(new TranslationTextComponent("geolosys.pro_pick.tooltip.nonefound_surface"), true);
        return false;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("deprecation")
    public void onDrawScreen(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || mc.gameSettings.showDebugInfo
                || mc.gameSettings.showDebugProfilerChart) {
            return;
        }
        if (mc.player.getHeldItemMainhand().getItem() instanceof ProPickItem
                || mc.player.getHeldItemOffhand().getItem() instanceof ProPickItem) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableLighting();
            int seaLvl = mc.player.getEntityWorld().getSeaLevel();
            int level = (int) (seaLvl - mc.player.getPosY());
            if (level < 0) {
                mc.fontRenderer.drawStringWithShadow(event.getMatrixStack(),
                        I18n.format("geolosys.pro_pick.depth.above", Math.abs(level)),
                        (float) ClientConfig.PROPICK_HUD_X.get(), (float) ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            } else if (level == 0) {
                mc.fontRenderer.drawStringWithShadow(event.getMatrixStack(), I18n.format("geolosys.pro_pick.depth.at"),
                        (float) ClientConfig.PROPICK_HUD_X.get(), (float) ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            } else {
                mc.fontRenderer.drawStringWithShadow(event.getMatrixStack(),
                        I18n.format("geolosys.pro_pick.depth.below", Math.abs(level)),
                        (float) ClientConfig.PROPICK_HUD_X.get(), (float) ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            }
            RenderSystem.color4f(1F, 1F, 1F, 1F);
        }
    }
}
