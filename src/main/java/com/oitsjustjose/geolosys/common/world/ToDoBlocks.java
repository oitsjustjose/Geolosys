package com.oitsjustjose.geolosys.common.world;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Thiakil on 17/06/2018.
 */
public class ToDoBlocks extends WorldSavedData
{
    private Map<ChunkPos, Map<BlockPos, IBlockState>> pendingBlocks = new HashMap<>();

    public ToDoBlocks(String name)
    {
        super(name);
    }

    public static ToDoBlocks getForWorld(World world, String dataID)
    {
        ToDoBlocks ret = (ToDoBlocks) world.loadData(ToDoBlocks.class, dataID);
        if (ret == null)
        {
            ret = new ToDoBlocks(dataID);
            world.setData(dataID, ret);
        }
        return ret;
    }

    public void storePending(BlockPos pos, IBlockState state)
    {
        Map<BlockPos, IBlockState> entries = pendingBlocks.computeIfAbsent(new ChunkPos(pos), k -> new HashMap<>());
        entries.put(pos.toImmutable(), state);
        markDirty();
    }

    public void processPending(ChunkPos pos, World world, Predicate<IBlockState> predicate)
    {
        Map<BlockPos, IBlockState> pending = pendingBlocks.get(pos);
        if (pending != null && !pending.isEmpty())
        {
            Iterator<Map.Entry<BlockPos, IBlockState>> iterator = pending.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<BlockPos, IBlockState> e = iterator.next();
                BlockPos blockPos = e.getKey();

                IBlockState state = world.getBlockState(blockPos);
                if (state.getBlock().isReplaceableOreGen(state, world, blockPos, predicate))
                {
                    world.setBlockState(blockPos, e.getValue(), 2 | 16);
                }
                iterator.remove();
            }
            if (pending.isEmpty())
            {
                pendingBlocks.remove(pos);
            }
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        pendingBlocks.clear();
        for (String key : nbt.getKeySet())
        {
            NBTBase val = nbt.getTag(key);
            if (val instanceof NBTTagList && ((NBTTagList) val).getTagType() == Constants.NBT.TAG_COMPOUND && !val.hasNoTags())
            {
                try
                {
                    Long asLong = Long.parseLong(key);
                    NBTTagList list = (NBTTagList) val;
                    ChunkPos chunkPos = new ChunkPos((int) (asLong & 4294967295L), (int) ((asLong >> 32) & 4294967295L));
                    Map<BlockPos, IBlockState> entries = new HashMap<>();
                    for (NBTBase b : list)
                    {
                        if (b instanceof NBTTagCompound && ((NBTTagCompound) b).hasKey("pos") && ((NBTTagCompound) b).hasKey("state"))
                        {
                            NBTTagCompound e = (NBTTagCompound) b;
                            entries.put(NBTUtil.getPosFromTag(e.getCompoundTag("pos")), NBTUtil.readBlockState(e.getCompoundTag("state")));
                        }
                    }
                    if (!entries.isEmpty())
                    {
                        pendingBlocks.put(chunkPos, entries);
                    }
                }
                catch (NumberFormatException e)
                {
                    //bad key, carry on
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        for (Map.Entry<ChunkPos, Map<BlockPos, IBlockState>> chunkEntry : pendingBlocks.entrySet())
        {
            if (!chunkEntry.getValue().isEmpty())
            {
                NBTTagList chunkEntries = new NBTTagList();
                compound.setTag(Long.toString(ChunkPos.asLong(chunkEntry.getKey().x, chunkEntry.getKey().z)), chunkEntries);
                for (Map.Entry<BlockPos, IBlockState> blockEntry : chunkEntry.getValue().entrySet())
                {
                    NBTTagCompound entry = new NBTTagCompound();
                    entry.setTag("pos", NBTUtil.createPosTag(blockEntry.getKey()));
                    entry.setTag("state", NBTUtil.writeBlockState(new NBTTagCompound(), blockEntry.getValue()));
                    chunkEntries.appendTag(entry);
                }
            }
        }
        return compound;
    }
}
