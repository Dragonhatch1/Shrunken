package com.xyrth.shrunken.client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class PlayerSizeHandler {

    private static float scale = ShrunkenState.getScale();
    private static float offset = ShrunkenState.getEyeOffset();
    private static boolean rendererSwapped = false;
    private static Method methodSetPlayerSize;
    private static float defaultWidth = 0.6F;
    private static float defaultHeight = 1.8F;
    private static float scaledStepHeight = 0.5F * scale;


    @SubscribeEvent
    public void onLivingRender(Pre event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;

        double trueY = player.posY + offset;
        int light = player.worldObj.getLightBrightnessForSkyBlocks(
            MathHelper.floor_double(player.posX),
            MathHelper.floor_double(trueY),
            MathHelper.floor_double(player.posZ),
            0
        );
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
    public void onLivingRender(Post event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) throws InvocationTargetException, IllegalAccessException {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;

        methodSetPlayerSize = ReflectionHelper.findMethod(
            Entity.class,
            player,
            new String[] { "setSize", "func_70105_a", "a" },
            new Class[] { Float.TYPE, Float.TYPE });

        methodSetPlayerSize.invoke(player, (defaultWidth * scale), (defaultHeight * scale));

        if (player.stepHeight != scaledStepHeight){
            player.stepHeight = scaledStepHeight;
        }

        //TODO Player Walking Speed

        //TODO Player Jump

    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (rendererSwapped) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        ShrunkenEntityRenderer renderer = new ShrunkenEntityRenderer(mc);
        renderer.setOffset(offset);
        mc.entityRenderer = renderer;


        rendererSwapped = true;
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event){
        if (!(event.entity instanceof EntityPlayer)) return;
        if (!"inWall".equals(event.source.getDamageType())) return;

        if (ShrunkenState.getScale() < 1.0F){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event){
        if (!(event.entity instanceof EntityPlayer)) return;
        if (!"inWall".equals(event.source.getDamageType())) return;

        if (ShrunkenState.getScale() < 1.0F){
            event.setCanceled(true);
        }
    }

}
