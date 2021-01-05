package com.oitsjustjose.geolosys.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class PeatBlock extends Block {
    public static final BooleanProperty TOP_BLOCK = BooleanProperty.create("is_top");

    public PeatBlock() {
        super(Properties.create(Material.EARTH, MaterialColor.DIRT).hardnessAndResistance(4F, 3F)
                .sound(SoundType.SOUL_SOIL).harvestTool(ToolType.SHOVEL));
        this.setDefaultState(this.stateContainer.getBaseState().with(TOP_BLOCK, Boolean.FALSE));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TOP_BLOCK);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (!context.getWorld().getBlockState(context.getPos().up()).isSolid()) {
            return this.getDefaultState().with(TOP_BLOCK, Boolean.TRUE);
        }
        return this.getDefaultState();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
            boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);

        if (!worldIn.getBlockState(pos.up()).isSolid() && state.get(TOP_BLOCK) != Boolean.TRUE) {
            worldIn.setBlockState(pos, state.with(TOP_BLOCK, Boolean.TRUE), 2 | 16);
        } else if (worldIn.getBlockState(pos.up()).isSolid() && state.get(TOP_BLOCK) != Boolean.FALSE) {
            worldIn.setBlockState(pos, state.with(TOP_BLOCK, Boolean.FALSE), 2 | 16);
        }
    }
}
