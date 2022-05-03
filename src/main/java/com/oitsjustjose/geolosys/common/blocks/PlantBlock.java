package com.oitsjustjose.geolosys.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.Random;

public class PlantBlock extends BushBlock {
    public RegistryObject<Block> placelist;
    private final boolean isExclusive;

    public PlantBlock(boolean exclusive, RegistryObject<Block> placeable) {
        super(Properties.of(Material.PLANT)
                .noCollission()
                .sound(SoundType.SWEET_BERRY_BUSH)
                .randomTicks());
        this.placelist = placeable;
        this.isExclusive = exclusive;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity && entityIn.getType() != EntityType.BEE) {
            entityIn.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockState below = worldIn.getBlockState(pos.below());
        if (this.isExclusive) {
            return placelist.get().equals(below.getBlock());
        }
        return super.canSurvive(state, worldIn, pos) || placelist.get().equals(below.getBlock());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
        if (this.placelist.get().equals(worldIn.getBlockState(pos.below()).getBlock())
                && worldIn.getRawBrightness(pos.above(), 0) >= 9
                && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt(10) == 0)) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (worldIn.getBlockState(pos.offset(x, 0, z)).isAir()
                            && this.placelist.get().equals(worldIn.getBlockState(pos.offset(x, -1, z)).getBlock())) {
                        worldIn.setBlock(pos.offset(x, 0, z), this.defaultBlockState(), 2 | 16);
                        return;
                    }
                }
            }
        }
    }
}
