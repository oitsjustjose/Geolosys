package com.oitsjustjose.geolosys.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class PeatBlock extends Block {

    public PeatBlock() {
        super(Properties.of(Material.DIRT, MaterialColor.GRASS).strength(4F, 3F).sound(SoundType.SOUL_SOIL));
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.BOTTOM, Boolean.TRUE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.BOTTOM);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (!context.getLevel().getBlockState(context.getClickedPos().above()).isSolidRender(context.getLevel(),
                context.getClickedPos())) {
            return this.defaultBlockState().setValue(BlockStateProperties.BOTTOM, Boolean.FALSE);
        }
        return this.defaultBlockState();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
            boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);

        if (!worldIn.getBlockState(pos.above()).isSolidRender(worldIn, pos.above())
                && state.getValue(BlockStateProperties.BOTTOM) != Boolean.FALSE) {
            worldIn.setBlock(pos, state.setValue(BlockStateProperties.BOTTOM, Boolean.FALSE), 2 | 16);
        } else if (worldIn.getBlockState(pos.above()).isSolidRender(worldIn, pos.above())
                && state.getValue(BlockStateProperties.BOTTOM) != Boolean.TRUE) {
            worldIn.setBlock(pos, state.setValue(BlockStateProperties.BOTTOM, Boolean.TRUE), 2 | 16);
        }
    }
}
