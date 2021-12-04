package com.oitsjustjose.geolosys.common.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PlantBlock extends BushBlock {
    public List<Block> placelist;
    private boolean isExclusive;

    public PlantBlock(boolean exclusive, Block... placeable) {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().sound(SoundType.SWEET_BERRY_BUSH)
                .tickRandomly());
        this.placelist = Arrays.asList(placeable);
        this.isExclusive = exclusive;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && entityIn.getType() != EntityType.BEE) {
            entityIn.setMotionMultiplier(state, new Vector3d((double) 0.8F, 0.75D, (double) 0.8F));
        }
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        if (this.isExclusive) {
            return placelist.contains(state.getBlock());
        }
        return super.isValidGround(state, worldIn, pos) || placelist.contains(state.getBlock());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (this.placelist.contains(worldIn.getBlockState(pos.down()).getBlock())
                && worldIn.getLightSubtracted(pos.up(), 0) >= 9
                && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt(10) == 0)) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (worldIn.getBlockState(pos.add(x, 0, z)).isAir()
                            && this.placelist.contains(worldIn.getBlockState(pos.add(x, -1, z)).getBlock())) {
                        worldIn.setBlockState(pos.add(x, 0, z), this.getDefaultState(), 2 | 16);
                        return;
                    }
                }
            }
        }
    }
}
