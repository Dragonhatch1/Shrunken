package com.xyrth.shrunken.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

import org.lwjgl.opengl.GL11;

import com.emoniph.witchery.client.RenderEntityViewer;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class PlayerSizeHandler {

    private static boolean rendererSwapped = false;
    private static Method methodSetPlayerSize;
    private static float defaultWidth = 0.6F;
    private static float defaultHeight = 1.8F;
    private static float scale = 0.25F; // 1.0 normal size | 1.5 50% bigger

    @SubscribeEvent
    public void onLivingRender(Pre event) {
        GL11.glTranslated(event.x, event.y, event.z);
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(-event.x, -event.y, -event.z);
    }

    @SubscribeEvent
    public void onLivingRender(Post event) {
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
        player.eyeHeight = defaultHeight * 0.92F;

    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (rendererSwapped) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (Loader.isModLoaded("witchery")) {
            RenderEntityViewer renderer = new RenderEntityViewer(mc);
            renderer.setOffset(0.9F);
            mc.entityRenderer = renderer;
        }


        rendererSwapped = true;
    }

}
