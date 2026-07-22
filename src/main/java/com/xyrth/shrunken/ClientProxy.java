package com.xyrth.shrunken;

import net.minecraftforge.common.MinecraftForge;

import com.xyrth.shrunken.client.PlayerRenderHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {

        super.init(event);

        PlayerRenderHandler playerRender = new PlayerRenderHandler();
        FMLCommonHandler.instance()
            .bus()
            .register(playerRender);
        MinecraftForge.EVENT_BUS.register(playerRender);
    }
}
