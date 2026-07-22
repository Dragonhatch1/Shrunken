package com.xyrth.shrunken.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class PlayerRenderHandler {

    private static float scale = ShrunkenState.getScale();
    private static float offset = ShrunkenState.getEyeOffset();
    private static boolean rendererSwapped = false;

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Pre event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;

        // Forces light to be calculated based off of our True Y Position with offset.
        double trueY = player.posY + offset;
        int light = player.worldObj.getLightBrightnessForSkyBlocks(
            MathHelper.floor_double(player.posX),
            MathHelper.floor_double(trueY),
            MathHelper.floor_double(player.posZ),
            0);
        int lightmapX = light % 65536;
        int lightmapY = light / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lightmapX, (float) lightmapY);

        GL11.glPushMatrix();
        GL11.glTranslated(0.0, offset, 0.0);
        GL11.glTranslated(event.x, event.y, event.z);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(-event.x, -event.y, -event.z);
    }

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Post event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (rendererSwapped) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // hooks into our custom camera and Y Offsets
        ShrunkenEntityRenderer renderer = new ShrunkenEntityRenderer(mc);
        renderer.setOffset(offset);
        mc.entityRenderer = renderer;

        rendererSwapped = true;
    }
}
