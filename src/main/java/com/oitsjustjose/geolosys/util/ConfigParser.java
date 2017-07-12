package com.oitsjustjose.geolosys.util;

import com.google.common.collect.Lists;
import com.oitsjustjose.geolosys.Geolosys;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.List;

public class ConfigParser
{
    private List<Entry> userEntriesClean;

    public ConfigParser()
    {
        userEntriesClean = Lists.newArrayList();
        parseConfig();
    }

    public void parseConfig()
    {
        for (String s : Geolosys.config.userEntriesRaw)
        {
            String[] parts = s.split("[\\W]");
            if (parts.length != 8)
            {
                printFormattingError(s);
                continue;
            }
            try
            {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (block == null || block == Blocks.AIR)
                {
                    printFormattingError(s);
                    continue;
                }
                IBlockState tempState = block.getStateForPlacement(null, null, null, 0.0F, 0.0F, 0.0F, toInt(parts[2]), null, null);
                userEntriesClean.add(new Entry(tempState, toInt(parts[3]), toInt(parts[4]), toInt(parts[5]), toInt(parts[6]), toInt(parts[7])));

            }
            catch (NumberFormatException e)
            {
                printFormattingError(s);
                continue;
            }
        }
    }

    private int toInt(String s)
    {
        return Integer.parseInt(s);
    }


    private void printFormattingError(String s)
    {
        Geolosys.LOGGER.info("Entry " + s + " is not valid and has been skipped. Please check your formatting.");
    }


    public List<Entry> getUserEntries()
    {
        return userEntriesClean;
    }


    public class Entry
    {
        private IBlockState state;
        private int size;
        private int minY;
        private int maxY;
        private int chunkOccurence;
        private int chancePerChunk;

        public Entry(IBlockState state, int size, int minY, int maxY, int chunkOccurence, int chancePerChunk)
        {
            this.state = state;
            this.size = size;
            this.minY = minY;
            this.maxY = maxY;
            this.chunkOccurence = chunkOccurence;
            this.chancePerChunk = chancePerChunk;
            sanitizeEntries();
        }

        private void sanitizeEntries()
        {
            if (this.minY < 0)
                this.minY = 0;
            if (this.maxY > 255)
                this.maxY = 255;
            if (this.chancePerChunk < 1)
                this.chancePerChunk = 1;
            if (this.chunkOccurence < 1)
                this.chunkOccurence = 1;
        }

        public IBlockState getState()
        {
            return state;
        }


        public int getSize()
        {
            return size;
        }

        public int getMinY()
        {
            return minY;
        }

        public int getMaxY()
        {
            return maxY;
        }

        public int getChunkOccurence()
        {
            return chunkOccurence;
        }

        public int getChancePerChunk()
        {
            return chancePerChunk;
        }
    }
}
