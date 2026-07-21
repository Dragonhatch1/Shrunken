package com.xyrth.shrunken;

import com.xyrth.shrunken.client.PlayerRenderHandler;
import com.xyrth.shrunken.client.PlayerSizeHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

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
