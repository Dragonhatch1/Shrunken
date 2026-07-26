package com.xyrth.shrunken.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketSyncScale implements IMessage {

    private float scale;

    public PacketSyncScale() {}

    public PacketSyncScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(scale);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        scale = buf.readFloat();
    }
}
