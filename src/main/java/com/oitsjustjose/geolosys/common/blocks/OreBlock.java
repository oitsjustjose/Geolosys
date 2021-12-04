package com.oitsjustjose.geolosys.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;

public class OreBlock extends Block {
    int xp;

    public OreBlock(Properties props, int experience) {
        super(props);
        this.xp = experience;
    }

    /**
     * Spawns the given amount of experience into the World as XP orb entities
     */
    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? MathHelper.nextInt(RANDOM, this.xp / 2, this.xp) : 0;
    }
}
