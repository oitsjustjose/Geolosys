package com.oitsjustjose.geolosys.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
    public int getExpDrop(BlockState state, LevelReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? Mth.nextInt(RANDOM, this.xp / 2, this.xp) : 0;
    }
}
