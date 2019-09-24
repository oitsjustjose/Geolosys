//package com.oitsjustjose.geolosys.common.world.capability;
//
//import com.oitsjustjose.geolosys.api.GeolosysAPI;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.nbt.INBT;
//import net.minecraft.util.Direction;
//import net.minecraftforge.common.capabilities.Capability;
//
//import javax.annotation.Nullable;
//import java.util.Map;
//
//public class Storage implements Capability.IStorage<Plutons>
//{
//    @Override
//    public void readNBT(Capability<Plutons> capability, Plutons instance, Direction side, INBT nbt)
//    {
//        if (compound.contains("currentWorldDeposits"))
//        {
//            CompoundNBT compDeposits = compound.getCompound("currentWorldDeposits");
//            for (String s : compDeposits.keySet())
//            {
//                GeolosysAPI.putWorldDeposit(s, compDeposits.getString(s));
//            }
//        }
//        if (compound.contains("regennedChunks"))
//        {
//            CompoundNBT compRegenned = compound.getCompound("regennedChunks");
//            for (String s : compRegenned.keySet())
//            {
//                if (compRegenned.getBoolean(s))
//                {
//                    GeolosysAPI.markChunkRegenned(s);
//                }
//            }
//        }
//    }
//
//    @Nullable
//    @Override
//    public INBT writeNBT(Capability<Plutons> capability, Plutons instance, Direction side)
//    {
//        if (!compound.contains("currentWorldDeposits"))
//        {
//            compound.put("currentWorldDeposits", new CompoundNBT());
//        }
//        CompoundNBT compDeposits = compound.getCompound("currentWorldDeposits");
//        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, String> e : GeolosysAPI.getCurrentWorldDeposits().entrySet())
//        {
//            compDeposits.putString(e.getKey().toString(), e.getValue());
//        }
//
//        if (!compound.contains("regennedChunks"))
//        {
//            compound.put("regennedChunks", new CompoundNBT());
//        }
//        CompoundNBT regenDeposits = compound.getCompound("regennedChunks");
//        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, Boolean> e : GeolosysAPI.getRegennedChunks().entrySet())
//        {
//            regenDeposits.putBoolean(e.getKey().toString(), e.getValue());
//        }
//
//        return compound;
//    }
//}
