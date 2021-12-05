package com.oitsjustjose.geolosys.api;

import com.oitsjustjose.geolosys.common.world.PlutonRegistry;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.ArrayList;

/**
 * The Geolosys API is intended for use by anyone who wants to tap into all the
 * locations that deposits exist Access is pretty easy, just reference this
 * class's currentWorldDeposits HashMap
 */
public class GeolosysAPI {
    // TODO: redo the GEolosys storage as per https://github.com/oitsjustjose/Persistent-Bits/blob/1.17/src/main/java/com/oitsjustjose/persistentbits/common/capability/ChunkLoaderList.java
    public static final Capability<IDepositCapability> GEOLOSYS_WORLD_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    // A collection of blocks to ignore in the OreConverter feature
    public static ArrayList<BlockState> oreConverterBlacklist = new ArrayList<>();
    // An instance of the registry for all generatable plutons
    public static PlutonRegistry plutonRegistry = new PlutonRegistry();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(plutonRegistry);
    }
}
