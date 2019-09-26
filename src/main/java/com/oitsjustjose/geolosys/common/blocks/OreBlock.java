package com.oitsjustjose.geolosys.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;

public class OreBlock extends Block
{
    private Block vanillaParent;

    public OreBlock(@Nullable Block parentBlock, Properties props)
    {
        super(props);
        this.vanillaParent = parentBlock;
    }

    /**
     * Spawns the given amount of experience into the World as XP orb entities
     */
    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch)
    {
        if (vanillaParent != null)
        {
            return vanillaParent.getExpDrop(state, reader, pos, fortune, silktouch);
        }
        return 0;
    }
}