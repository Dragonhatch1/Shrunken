package com.xyrth.shrunken.network;

import com.xyrth.shrunken.util.Config;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncScaleHandler implements IMessageHandler<PacketSyncScale, IMessage> {

    @Override
    public IMessage onMessage(PacketSyncScale message, MessageContext ctx) {
        Config.scale = message.getScale();
        return null;
    }
}
