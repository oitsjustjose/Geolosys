package com.oitsjustjose.geolosys.common.blocks;

import java.util.Random;

import javax.annotation.Nonnull;

import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class SampleBlock extends Block implements IWaterLoggable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Properties BASE_PROPS = Properties.create(Material.EARTH, MaterialColor.LIGHT_GRAY)
            .hardnessAndResistance(0.125F, 2F).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL);

    public SampleBlock() {
        super(CommonConfig.SAMPLE_TICK_ENABLED.get() ? BASE_PROPS.tickRandomly() : BASE_PROPS);
        this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getWorld().getBlockState(context.getPos()).getBlock() == Blocks.WATER) {
            return this.getDefaultState().with(WATERLOGGED, Boolean.TRUE);
        }
        return this.getDefaultState();
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Vector3d offset = state.getOffset(worldIn, pos);
        return VoxelShapes.create(0.2D, 0.0D, 0.2D, 0.8D, 0.2D, 0.8D).withOffset(offset.x, offset.y, offset.z);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        // One in ten chance for the sample to break when fallen on
        Random random = new Random();
        if (((int) fallDistance) > 0) {
            if (random.nextInt((int) fallDistance) > 5) {
                worldIn.destroyBlock(pos, true);
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
            Hand handIn, BlockRayTraceResult hit) {
        if (!player.isCrouching()) {
            worldIn.destroyBlock(pos, true);
            player.swingArm(handIn);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return Block.hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    @Nonnull
    public Block.OffsetType getOffsetType() {
        return Block.OffsetType.XZ;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
            boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if (!this.isValidPosition(state, worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        } else if (state.get(WATERLOGGED)) { // Update the water from flowing to still or vice-versa
            worldIn.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
    }

    public BlockState asWaterlogged() {
        return this.getDefaultState().with(WATERLOGGED, Boolean.TRUE);
    }

    // Only tick randomly whenever not waterlogged, to make it waterlogged.
    public boolean ticksRandomly(BlockState state) {
        return CommonConfig.SAMPLE_TICK_ENABLED.get()
                && (state.hasProperty(WATERLOGGED) && state.get(WATERLOGGED).equals(Boolean.FALSE));
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 1)) {
            return;
        }

        BlockState[] neighbors = new BlockState[] { worldIn.getBlockState(pos.add(1, 0, 0)),
                worldIn.getBlockState(pos.add(-1, 0, 0)), worldIn.getBlockState(pos.add(0, 0, 1)),
                worldIn.getBlockState(pos.add(0, 0, -1)) };

        int waterNeighbors = 0;
        for (BlockState b : neighbors) {
            if (b.getFluidState() == Fluids.WATER.getStillFluidState(false)) {
                waterNeighbors++;
            }
        }

        if (waterNeighbors > 1) {
            worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.TRUE), 2 | 16);
        }
    }
}
