package com.oitsjustjose.geolosys.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount)
    {
        if (vanillaParent != null)
        {
            vanillaParent.dropXpOnBlockBreak(worldIn, pos, amount);
        }
    }
}