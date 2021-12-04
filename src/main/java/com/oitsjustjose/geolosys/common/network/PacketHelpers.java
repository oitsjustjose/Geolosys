package com.oitsjustjose.geolosys.common.network;

import java.util.HashSet;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

public class PacketHelpers {
    public static final String BLOCK_NBT_NAME = "blocks";
    public static final String BLOCKPOS_NBT_NAME = "positions";

    public static CompoundNBT encodeBlocks(HashSet<BlockState> blocks) {
        CompoundNBT comp = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (BlockState b : blocks) {
            list.add(NBTUtil.writeBlockState(b));
        }
        comp.put(BLOCK_NBT_NAME, list);
        return comp;
    }

    public static HashSet<BlockState> decodeBlocks(CompoundNBT comp) {
        HashSet<BlockState> ret = new HashSet<BlockState>();
        ListNBT list = comp.getList(BLOCK_NBT_NAME, 10);
        list.forEach((c) -> {
            if (c instanceof CompoundNBT) {
                ret.add(NBTUtil.readBlockState((CompoundNBT) c));
            } else {
                Geolosys.getInstance().LOGGER.error("The following compound appears to be broken: {}", c);
            }
        });

        return ret;
    }

    public static CompoundNBT encodeBlockPosns(HashSet<BlockPos> pos) {
        CompoundNBT comp = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (BlockPos p : pos) {
            list.add(NBTUtil.writeBlockPos(p));
        }
        comp.put(BLOCKPOS_NBT_NAME, list);
        return comp;
    }

    public static HashSet<BlockPos> decodeBlockPosns(CompoundNBT comp) {
        HashSet<BlockPos> ret = new HashSet<BlockPos>();
        ListNBT list = comp.getList(BLOCKPOS_NBT_NAME, 10);
        list.forEach((c) -> {
            if (c instanceof CompoundNBT) {
                ret.add(NBTUtil.readBlockPos((CompoundNBT) c));
            } else {
                Geolosys.getInstance().LOGGER.error("The following compound appears to be broken: {}", c);
            }
        });
        return ret;
    }

    public static String messagify(HashSet<BlockState> blocks) {
        StringBuilder sb = new StringBuilder();
        // I hate old for-loops but I need the index.
        int idx = 0;
        for (BlockState b : blocks) {
            sb.append((new ItemStack(b.getBlock())).getDisplayName().getString());
            if ((idx + 2) == blocks.size()) {
                sb.append(" & ");
            } else if ((idx + 1) != blocks.size()) {
                sb.append(", ");
            }
            idx++;
        }
        return sb.toString();
    }
}
