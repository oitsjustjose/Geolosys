package com.oitsjustjose.geolosys.common.items;

import java.util.HashSet;

import com.mojang.blaze3d.platform.GlStateManager;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import com.oitsjustjose.geolosys.common.utils.Prospecting;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ProPickItem extends Item {
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Constants.MODID, "prospectors_pick");
    public static Item.Properties props = CommonConfig.ENABLE_PRO_PICK_DMG.get()
            ? new Item.Properties().stacksTo(1).tab(GeolosysGroup.getInstance())
                    .durability(CommonConfig.PRO_PICK_DURABILITY.get())
            : new Item.Properties().stacksTo(1).tab(GeolosysGroup.getInstance());

    public ProPickItem() {
        super(props);
        this.setRegistryName(REGISTRY_NAME);
        Geolosys.proxy.registerClientSubscribeEvent(this);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();

        if (player.isCrouching()) {
            return InteractionResult.CONSUME;
        } else {
            ItemStack stack = player.getItemInHand(hand);

            if (worldIn.isClientSide) {
                player.swing(hand);
                return InteractionResult.CONSUME;
            }

            if (!player.isCreative()) {
                stack.hurtAndBreak(1, player, (x) -> x.broadcastBreakEvent(hand));
            }

            int range = CommonConfig.PRO_PICK_RANGE.get();
            int diam = CommonConfig.PRO_PICK_DIAMETER.get();

            int zStart = facing == Direction.NORTH ? 0 : facing == Direction.SOUTH ? -range : -(diam / 2);
            int zEnd = facing == Direction.NORTH ? range : facing == Direction.SOUTH ? 0 : diam / 2;
            int xStart = facing == Direction.EAST ? -range : facing == Direction.WEST ? 0 : -(diam / 2);
            int xEnd = facing == Direction.EAST ? 0 : facing == Direction.WEST ? range : diam / 2;
            int yStart = facing == Direction.UP ? -range : facing == Direction.DOWN ? 0 : -(diam / 2);
            int yEnd = facing == Direction.UP ? 0 : facing == Direction.DOWN ? range : diam / 2;

            prospect(player, stack, worldIn, pos, facing, xStart, xEnd, yStart, yEnd, zStart, zEnd);
            player.swing(hand);
        }
        return InteractionResult.CONSUME;
    }

    private boolean prospect(Player player, ItemStack stack, Level level, BlockPos pos, Direction facing,
            int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd) {

        HashSet<BlockState> foundBlocks = new HashSet<>();
        HashSet<BlockPos> foundBlockPos = new HashSet<>();
        HashSet<BlockState> depositBlocks = Prospecting.getDepositBlocks();

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                for (int z = zStart; z <= zEnd; z++) {
                    BlockPos tmpPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(tmpPos);
                    if (depositBlocks.contains(state) && !Prospecting.isBlacklistedFromDetection(state)) {
                        foundBlocks.add(state);
                        foundBlockPos.add(tmpPos);
                    }
                }
            }
        }

        if (!foundBlocks.isEmpty()) {
            Geolosys.proxy.sendProspectingMessage(player, foundBlocks, facing.getOpposite());
            foundBlockPos.forEach((_pos) -> {
                level.playSound(null, _pos, SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 0.15F, 2F);
            });
            return true;
        }
        return prospectChunk(level, stack, pos, player);
    }

    private boolean prospectChunk(Level level, ItemStack stack, BlockPos pos, Player player) {
        HashSet<BlockState> foundBlocks = new HashSet<BlockState>();
        HashSet<BlockState> depositBlocks = Prospecting.getDepositBlocks();
        ChunkPos tempPos = new ChunkPos(pos);

        for (int x = tempPos.getMinBlockX(); x <= tempPos.getMaxBlockX(); x++) {
            for (int z = tempPos.getMinBlockZ(); z <= tempPos.getMaxBlockZ(); z++) {
                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
                    BlockState state = level.getBlockState(new BlockPos(x, y, z));
                    if (depositBlocks.contains(state) && !Prospecting.isBlacklistedFromDetection(state)) {
                        foundBlocks.add(state);
                    }
                }
            }
        }

        if (!foundBlocks.isEmpty()) {
            Geolosys.proxy.sendProspectingMessage(player, foundBlocks, null);
            return true;
        }

        player.displayClientMessage(new TranslatableComponent("geolosys.pro_pick.tooltip.nonefound_surface"), true);
        return false;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onDrawScreen(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || mc.options.renderDebug
                || mc.options.renderDebugCharts) {
            return;
        }
        if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ProPickItem
                || mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ProPickItem) {
            GlStateManager._enableBlend();
            GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int seaLvl = mc.player.getLevel().getSeaLevel();
            int level = (int) (seaLvl - mc.player.getY());
            if (level < 0) {
                mc.font.draw(event.getMatrixStack(),
                        I18n.get("geolosys.pro_pick.depth.above", Math.abs(level)),
                        (float) ClientConfig.PROPICK_HUD_X.get(), (float) ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            } else if (level == 0) {
                mc.font.draw(event.getMatrixStack(), I18n.get("geolosys.pro_pick.depth.at"),
                        (float) ClientConfig.PROPICK_HUD_X.get(), (float) ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            } else {
                mc.font.draw(event.getMatrixStack(),
                        I18n.get("geolosys.pro_pick.depth.below", Math.abs(level)),
                        (float) ClientConfig.PROPICK_HUD_X.get(), (float) ClientConfig.PROPICK_HUD_Y.get(), 0xFFFFFFFF);
            }
        }
    }
}
