package com.oitsjustjose.geolosys.common.items;

import com.mojang.blaze3d.platform.GlStateManager;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import com.oitsjustjose.geolosys.common.utils.Prospecting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import org.lwjgl.opengl.GL11;

import java.util.HashSet;

public class ProPickItem extends Item {
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Constants.MODID, "prospectors_pick");

    public ProPickItem() {
        super(new Item.Properties().stacksTo(1).tab(GeolosysGroup.getInstance()));
        this.setRegistryName(REGISTRY_NAME);
        Geolosys.proxy.registerClientSubscribeEvent(this);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (CommonConfig.ENABLE_PRO_PICK_DMG.get()) {
            if (stack.getTag() == null) {
                stack.setTag(new CompoundTag());
                stack.getTag().putInt("damage", CommonConfig.PRO_PICK_DURABILITY.get());
            }
            return 1 - stack.getTag().getInt("damage") / CommonConfig.PRO_PICK_DURABILITY.get();
        } else {
            return 1;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();

        if (player.isCrouching()) {
            return InteractionResult.PASS;
        } else {
            if (!player.isCreative()) {
                // TODO: Work on manually implementing damage
                // this.attemptDamageItem(player, pos, hand, worldIn);
            }
            // At surface or higher
            if (worldIn.isClientSide) {
                player.swing(hand);
                return InteractionResult.PASS;
            }

            ItemStack stack = player.getItemInHand(hand);

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

            player.swing(hand);
        }
        return InteractionResult.SUCCESS;
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
                for (int y = 0; y < level.getHeight(); y++) {
                    BlockState state = level.getBlockState(new BlockPos(x, y, z));
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
            // RenderSystem.disableLighting();
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
            // RenderSystem.color4f(1F, 1F, 1F, 1F);
        }
    }
}
