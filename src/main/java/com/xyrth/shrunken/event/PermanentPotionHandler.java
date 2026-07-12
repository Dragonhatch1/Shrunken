package com.xyrth.shrunken.event;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.client.event.RenderLivingEvent.Post;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PermanentPotionHandler {

    private static Method methodEntitySetSize;


    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) throws InvocationTargetException, IllegalAccessException {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;

        // Only run server-side to avoid double application
        if (player.worldObj.isRemote) return;

        player.eyeHeight = 0.03F;
            player.getEyeHeight();

        float width = 0.50F;
        float height = 0.50F;

        methodEntitySetSize = ReflectionHelper.findMethod(Entity.class, event.entity, new String[]{"setSize", "func_70105_a", "a"}, new Class[]{Float.TYPE, Float.TYPE});
        System.out.println(methodEntitySetSize);
        methodEntitySetSize.invoke(event.entity, new Object[]{width, height});
        System.out.println("Current Height:" + event.entity.height);


    }
    @SubscribeEvent
    public void onLivingRender(Pre event) {
        GL11.glPushMatrix();
        GL11.glTranslated(event.x, event.y, event.z);
        float scale = 0.25F;
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(-event.x, -event.y, -event.z);
    }

    public void onLivingRender(World world, EntityLivingBase entity, Post event) {
        GL11.glPopMatrix();
    }
}
