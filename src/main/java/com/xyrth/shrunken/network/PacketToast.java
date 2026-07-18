package com.xyrth.shrunken.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketToast implements IMessage {

    public String message;
    public int durationTicks;
    public boolean zone;

    public PacketToast() {} //required no-arg constructor

    public PacketToast(String message, int durationTicks, boolean zone){
        this.message = message;
        this.durationTicks = durationTicks;
        this.zone = zone;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);
        buf.writeInt(durationTicks);
        buf.writeBoolean(zone);
    }

    @Override
    public void fromBytes(ByteBuf buf){
        message = ByteBufUtils.readUTF8String(buf);
        durationTicks = buf.readInt();
        zone = buf.readBoolean();
    }
}
