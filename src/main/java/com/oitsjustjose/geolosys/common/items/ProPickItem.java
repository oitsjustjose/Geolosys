package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import com.oitsjustjose.geolosys.common.utils.Prospecting;
import com.oitsjustjose.geolosys.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class ProPickItem extends Item {
    public static Item.Properties props = CommonConfig.ENABLE_PRO_PICK_DMG.get() ? new Item.Properties().stacksTo(1).tab(GeolosysGroup.getInstance()).durability(CommonConfig.PRO_PICK_DURABILITY.get()) : new Item.Properties().stacksTo(1).tab(GeolosysGroup.getInstance());

    public ProPickItem() {
        super(props);
        Geolosys.proxy.registerClientSubscribeEvent(this);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();

        if (player == null) {
            return InteractionResult.PASS;
        }

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

    private void prospect(Player player, ItemStack stack, Level level, BlockPos pos, Direction facing, int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd) {
        HashSet<BlockState> foundBlocks = new HashSet<>();
        HashSet<BlockPos> foundBlockPos = new HashSet<>();
        HashSet<BlockState> depositBlocks = Prospecting.getDepositBlocks();

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                for (int z = zStart; z <= zEnd; z++) {
                    BlockPos tmpPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(tmpPos);
                    if (depositBlocks.contains(state) && Prospecting.canDetect(state)) {
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
            return;
        }
        prospectChunk(level, pos, player);
    }

    private void prospectChunk(Level level, BlockPos pos, Player player) {
        HashSet<BlockState> foundBlocks = new HashSet<BlockState>();
        HashSet<BlockState> depositBlocks = Prospecting.getDepositBlocks();
        ChunkPos tempPos = new ChunkPos(pos);

        for (int x = tempPos.getMinBlockX(); x <= tempPos.getMaxBlockX(); x++) {
            for (int z = tempPos.getMinBlockZ(); z <= tempPos.getMaxBlockZ(); z++) {
                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
                    BlockState state = level.getBlockState(new BlockPos(x, y, z));
                    if (depositBlocks.contains(state) && Prospecting.canDetect(state)) {
                        foundBlocks.add(state);
                    }
                }
            }
        }

        if (!foundBlocks.isEmpty()) {
            Geolosys.proxy.sendProspectingMessage(player, foundBlocks, null);
            return;
        }

        player.displayClientMessage(Utils.tryTranslate("geolosys.pro_pick.tooltip.nonefound_surface"), true);
    }
}
