package com.xyrth.shrunken;

import com.xyrth.shrunken.client.PlayerSizeHandler;
import com.xyrth.shrunken.command.CommandBreakroom;
import com.xyrth.shrunken.util.Config;
import com.xyrth.shrunken.util.LogUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        // Initializing the network wrapper
        // SimpleNetworkWrapper networkRegistry = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        // networkRegistry.registerMessage(ShrunkenPacketHandler.class, ShrunkenPacket.class, 0, Side.SERVER);

        LogUtil.info("Shrunken is running at " + Tags.VERSION);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        // Event Handlers
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandBreakroom());
    }

}
