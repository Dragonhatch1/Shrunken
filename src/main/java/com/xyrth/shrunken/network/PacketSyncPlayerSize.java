package com.xyrth.shrunken.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketSyncPlayerSize implements IMessage {

    private float width;
    private float height;
    private float stepHeight;

    public PacketSyncPlayerSize() {}

    public PacketSyncPlayerSize(float width, float height, float stepHeight) {
        this.width = width;
        this.height = height;
        this.stepHeight = stepHeight;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getStepHeight() {
        return stepHeight;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(width);
        buf.writeFloat(height);
        buf.writeFloat(stepHeight);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        width = buf.readFloat();
        height = buf.readFloat();
        stepHeight = buf.readFloat();
    }
}
