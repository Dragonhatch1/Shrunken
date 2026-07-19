package com.xyrth.shrunken.network;

import com.xyrth.shrunken.event.BreakroomHandler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketToastHandler implements IMessageHandler<PacketToast, IMessage> {

    @Override
    public IMessage onMessage(PacketToast message, MessageContext ctx) {
        BreakroomHandler.toastHandler.showToast(message.message, message.durationTicks, message.zone);
        return null;
    }
}
