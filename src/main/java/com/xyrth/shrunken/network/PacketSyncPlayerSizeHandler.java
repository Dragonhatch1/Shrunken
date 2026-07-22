package com.xyrth.shrunken.network;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncPlayerSizeHandler implements IMessageHandler<PacketSyncPlayerSize, IMessage> {

    private static Method methodSetPlayerSize;

    @Override
    public IMessage onMessage(PacketSyncPlayerSize message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        // if (player == null) return null;
        //
        // try{
        // if (methodSetPlayerSize == null) {
        // methodSetPlayerSize = ReflectionHelper.findMethod(
        // Entity.class,
        // player,
        // new String[] { "setSize", "func_70105_a", "a" },
        // new Class[] { Float.TYPE, Float.TYPE });
        // }
        //
        // methodSetPlayerSize.invoke(player, message.getWidth(), message.getHeight());
        // } catch (Exception e) {}
        //
        // player.stepHeight = message.getStepHeight();
        return null;
    }
}
