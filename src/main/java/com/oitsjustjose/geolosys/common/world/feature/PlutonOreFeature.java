package com.oitsjustjose.geolosys.common.world.feature;

import com.mojang.datafixers.Dynamic;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.blocks.SampleBlock;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.PlutonRegistry;
import com.oitsjustjose.geolosys.common.world.capability.IPlutonCapability;
import com.oitsjustjose.geolosys.common.world.utils.ChunkPosDim;
import com.oitsjustjose.geolosys.common.world.utils.SampleUtils;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlutonOreFeature extends Feature<NoFeatureConfig>
{
    public PlutonOreFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
    {
        super(configFactoryIn, true);
    }

    /**
     * Post-pluton-placement logic to generate samples and more
     *
     * @param world an IWorld instance
     * @param pos   The BlockPos from which the pluton originated
     * @param ore   The IDeposit that was generated
     */
    private void postPlacement(IWorld world, BlockPos pos, IDeposit ore)
    {
        IPlutonCapability plutonCapability = world.getWorld().getCapability(GeolosysAPI.PLUTON_CAPABILITY).orElse(null);
        if (plutonCapability != null)
        {
            plutonCapability.setGenerated(new ChunkPosDim(pos, Objects.requireNonNull(world.getDimension().getType().getRegistryName()).toString()));
        }
        if (world.getWorld().getWorldType() != WorldType.FLAT)
        {
            int sampleLimit = SampleUtils.getSampleCount(ore);
            for (int i = 0; i < sampleLimit; i++)
            {
                BlockPos samplePos = SampleUtils.getSamplePosition(world, new ChunkPos(pos), ore.getYMax());
                if (samplePos == null || SampleUtils.inNonWaterFluid(world, samplePos))
                {
                    continue;
                }
                if (!(world.getBlockState(samplePos).getBlock() instanceof SampleBlock))
                {
                    BlockState sampleState =
                            SampleUtils.isInWater(world, samplePos) ?
                                    ore.getSample().with(SampleBlock.WATERLOGGED, Boolean.TRUE) :
                                    ore.getSample();
                    world.setBlockState(samplePos, sampleState, 2 | 16);
                }
            }
        }
    }

    private boolean isInChunk(ChunkPos chunkPos, BlockPos pos)
    {
        int blockX = pos.getX();
        int blockZ = pos.getZ();
        return blockX >= chunkPos.getXStart() && blockX <= chunkPos.getXEnd() && blockZ >= chunkPos.getZStart()
                && blockZ <= chunkPos.getZEnd();
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
            BlockPos pos, NoFeatureConfig config)
    {
        IPlutonCapability plutonCapability = worldIn.getWorld().getCapability(GeolosysAPI.PLUTON_CAPABILITY).orElse(null);
        if (plutonCapability == null)
        {
            Geolosys.getInstance().LOGGER.error("No PlutonCapability present -- things will likely break.");
            return false;
        }
        ChunkPosDim chunkPosDim = new ChunkPosDim(pos, Objects.requireNonNull(worldIn.getDimension().getType().getRegistryName()).toString());
        if (plutonCapability.hasGenerated(chunkPosDim))
        {
            return false;
        }
        IDeposit pluton = PlutonRegistry.getInstance().pickPluton();
        // Logic to confirm that this can be placed here
        if (pluton instanceof DepositBiomeRestricted)
        {
            DepositBiomeRestricted restricted = (DepositBiomeRestricted) pluton;
            if (!restricted.canPlaceInBiome(worldIn.getBiome(pos)))
            {
                return false;
            }
        }
        else if (pluton instanceof DepositMultiOreBiomeRestricted)
        {
            DepositMultiOreBiomeRestricted restricted = (DepositMultiOreBiomeRestricted) pluton;
            if (!restricted.canPlaceInBiome(worldIn.getBiome(pos)))
            {
                return false;
            }
        }

        // New way of determining if the dimension is valid for generation
        // Much quicker to use parallel streams than a for-loop, especially if in a large modpack
        List<DimensionType> dimTypes = Arrays.stream(pluton.getDimensionBlacklist()).parallel().map(
                x -> DimensionType.byName(new ResourceLocation(x))
        ).collect(Collectors.toList());

        if (dimTypes.contains(worldIn.getDimension().getType()))
        {
            return false;
        }

        float f = rand.nextFloat() * (float) Math.PI;
        double d0 = (float) (pos.getX() + 8) + MathHelper.sin(f) * (float) pluton.getSize() / 8.0F;
        double d1 = (float) (pos.getX() + 8) - MathHelper.sin(f) * (float) pluton.getSize() / 8.0F;
        double d2 = (float) (pos.getZ() + 8) + MathHelper.cos(f) * (float) pluton.getSize() / 8.0F;
        double d3 = (float) (pos.getZ() + 8) - MathHelper.cos(f) * (float) pluton.getSize() / 8.0F;
        double d4 = pos.getY() + rand.nextInt(3) - 2;
        double d5 = pos.getY() + rand.nextInt(3) - 2;

        // ToDoBlocks toDoBlocks = ToDoBlocks.getForWorld(worldIn, dataName);
        ChunkPos thisChunk = new ChunkPos(pos);
        boolean placed = false;

        for (int i = 0; i < pluton.getSize(); ++i)
        {
            float f1 = (float) i / (float) pluton.getSize();
            double d6 = d0 + (d1 - d0) * (double) f1;
            double d7 = d4 + (d5 - d4) * (double) f1;
            double d8 = d2 + (d3 - d2) * (double) f1;
            double d9 = rand.nextDouble() * (double) pluton.getSize() / 16.0D;
            double d10 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (double) (MathHelper.sin((float) Math.PI * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1)
            {
                double d12 = ((double) l1 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D)
                {
                    for (int i2 = k; i2 <= j1; ++i2)
                    {
                        double d13 = ((double) i2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D)
                        {
                            for (int j2 = l; j2 <= k1; ++j2)
                            {
                                double d14 = ((double) j2 + 0.5D - d8) / (d10 / 2.0D);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D)
                                {
                                    BlockPos blockpos = new BlockPos(l1, i2, j2);

                                    if (isInChunk(thisChunk, blockpos) || worldIn.chunkExists(l1 >> 4, j2 >> 4))
                                    {
                                        float density = Math.min(pluton.getDensity(), 1.0F);

                                        if (rand.nextFloat() > density)
                                        {
                                            continue;
                                        }
                                        BlockState state = worldIn.getBlockState(blockpos);
                                        // If it has custom blockstate matcher:
                                        if (pluton.getBlockStateMatchers() != null)
                                        {
                                            for (BlockState BlockState : pluton.getBlockStateMatchers())
                                            {
                                                if (Utils.doStatesMatch(BlockState, state))
                                                {
                                                    worldIn.setBlockState(blockpos, pluton.getOre(), 2 | 16);
                                                    placed = true;
                                                    break;
                                                }
                                            }
                                        }
                                        // Otherwise just use the default
                                        else
                                        {
                                            for (BlockState BlockState : GeolosysAPI.replacementMats)
                                            {
                                                if (Utils.doStatesMatch(BlockState, state))
                                                {
                                                    worldIn.setBlockState(blockpos, pluton.getOre(), 2 | 16);
                                                    placed = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        // TODO: Fix toDoBlocks
                                        // toDoBlocks.storePending(blockpos, ore.getOre());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (placed)
        {
            this.postPlacement(worldIn, pos, pluton);
        }

        return placed;
    }
}