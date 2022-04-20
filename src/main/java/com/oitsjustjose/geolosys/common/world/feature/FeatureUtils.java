package com.oitsjustjose.geolosys.common.world.feature;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FeatureUtils {
    private static boolean ensureCanWriteNoThrow(WorldGenLevel level, BlockPos pos)
    {
        if (level instanceof WorldGenRegion region)
        {
            ChunkPos center = region.getCenter();
            int i = SectionPos.blockToSectionCoord(pos.getX());
            int j = SectionPos.blockToSectionCoord(pos.getZ());
            int k = Math.abs(center.x - i);
            int l = Math.abs(center.z - j);
            // writeRadiusCutoff is not accessible, so use a constant 1 for 3x3 generation.
            if (k > 1 || l > 1)
            {
                return false;
            }
            return true;
        }
        else
        {
            // All feature levels *should* be WorldGenRegions (this has not thrown yet)
            Geolosys.getInstance().LOGGER.error("level was not WorldGenRegion");
            return false;
        }
    }
    
    public static boolean tryPlaceBlock(WorldGenLevel level, ChunkPos chunk, BlockPos pos, BlockState state,
            IDepositCapability cap) {

        if(!ensureCanWriteNoThrow(level, pos))
        {
            cap.putPendingBlock(new BlockPos(pos), state);
            return false;
        }

        if (!level.setBlock(pos, state, 2 | 16)) {
            cap.putPendingBlock(new BlockPos(pos), state);
            return false;
        }
        return true;
    }

    public static void fixSnowyBlock(WorldGenLevel level, BlockPos posPlaced) {
        BlockState below = level.getBlockState(posPlaced.below());
        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            level.setBlock(posPlaced.below(), below.setValue(BlockStateProperties.SNOWY, Boolean.valueOf(false)),
                    2 | 16);
        }
    }
}
