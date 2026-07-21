package com.xyrth.shrunken.client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class PlayerSizeHandler {

    private static float scale = ShrunkenState.getScale();
    private static Method methodSetPlayerSize;
    private static float defaultWidth = 0.6F;
    private static float defaultHeight = 1.8F;
    private static float scaledStepHeight = 0.5F * scale;
    private static Field fieldEyeHeight;
    private static float lastScale = -1.0F;


    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) throws InvocationTargetException, IllegalAccessException {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;

        //Sets player size, if set every tick you will get swimming issues. Set once or call when needed.
        if (scale != lastScale) {
            if (methodSetPlayerSize == null) {
                methodSetPlayerSize = ReflectionHelper.findMethod(
                    Entity.class,
                    player,
                    new String[]{"setSize", "func_70105_a", "a"},
                    new Class[]{Float.TYPE, Float.TYPE});
            }


            methodSetPlayerSize.invoke(player, (defaultWidth * scale), (defaultHeight * scale));
            lastScale = scale;
        }

        //Change stepHeight if it's not the scaled step height
        if (player.stepHeight != scaledStepHeight) {
            player.stepHeight = scaledStepHeight;
        }

        if (!player.worldObj.isRemote) {
            if (fieldEyeHeight == null) {
                fieldEyeHeight = ReflectionHelper.findField(EntityPlayer.class, "eyeHeight");
                fieldEyeHeight.setAccessible(true);
            }

            fieldEyeHeight.setFloat(player, (defaultHeight * 0.9F) * scale);

        }

        //Change Player Speed if Scale is above 1
        if (!player.worldObj.isRemote) {
            if (scale > 1.0F) {
                IAttributeInstance speedAttribute = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                double vanillaSpeed = 0.1D;
                speedAttribute.setBaseValue(Math.min((double) vanillaSpeed * scale, 3.0D));
            }
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingJumpEvent event){
        if (!(event.entity instanceof EntityPlayer)) return;

        //change jump distance based on scale with a bottom of 0.5D and top of 2.0D
        float scale = ShrunkenState.getScale();
        double jumpMultiplier = Math.min(Math.max((double)scale, 0.5D) * 1.5D, 2.0);
        event.entityLiving.motionY *= jumpMultiplier;
    }


    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        if (!"inWall".equals(event.source.getDamageType())) return;

        // ignores damage from any source that is labeled "inWall"
        if (ShrunkenState.getScale() < 1.0F) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        if (!"inWall".equals(event.source.getDamageType())) return;

        //Ignores the attack event if source is labeled "inWall"
        if (ShrunkenState.getScale() < 1.0F) {
            event.setCanceled(true);
        }
    }

}
