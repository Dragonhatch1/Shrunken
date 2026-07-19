package com.xyrth.shrunken.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import javafx.scene.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class PlayerSizeHandler {

    private static Method methodSetPlayerSize;
    private static float defaultWidth = 0.6F;
    private static float defaultHeight= 1.8F;
    private static float scale = 0.25F; // 1.0 normal size | 1.5 50% bigger

    @SubscribeEvent
    public void onLivingRender(Pre event){
        GL11.glTranslated(event.x, event.y, event.z);
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(-event.x, -event.y, -event.z);
    }

    @SubscribeEvent
    public void onLivingRender(Post event){
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) throws InvocationTargetException, IllegalAccessException {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;

        methodSetPlayerSize = ReflectionHelper.findMethod(Entity.class, player, new String[]{"setSize", "func_70105_a", "a"}, new Class[]{Float.TYPE, Float.TYPE});

        methodSetPlayerSize.invoke(player, (defaultWidth * scale), (defaultHeight * scale));
        player.eyeHeight = defaultHeight * scale;
    }


}
