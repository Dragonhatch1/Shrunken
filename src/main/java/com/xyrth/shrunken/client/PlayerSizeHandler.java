package com.xyrth.shrunken.client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class PlayerSizeHandler {

    private static float scale = ShrunkenState.getScale();
    private static Method methodSetPlayerSize;
    private static float scaledStepHeight = 0.5F * scale; // default step height is 0.5F
    private static float scaledHeight = 1.8F * scale; // default height is 1.8F
    private static float scaledWidth = 0.6F * scale; // default width is 0.6F

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) throws InvocationTargetException, IllegalAccessException {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;

        // Sets player size, eye height, and step height
        if (player.worldObj.isRemote || player.ticksExisted % 20 == 0) {
            float currentHeight = player.height;
            if (scaledHeight != currentHeight) {
                if (methodSetPlayerSize == null) {
                    methodSetPlayerSize = ReflectionHelper.findMethod(
                        Entity.class,
                        event.entity,
                        new String[] { "setSize", "func_70105_a", "a" },
                        new Class[] { Float.TYPE, Float.TYPE });
                }

                if (!player.worldObj.isRemote) {
                    player.eyeHeight = scaledHeight * 0.9F;
                }

                player.stepHeight = scaledStepHeight;
                methodSetPlayerSize.invoke(event.entity, (scaledWidth), (scaledHeight));
            }
        }

        // Change Player Speed if Scale is above 1
        if (!player.worldObj.isRemote) {
            if (scale > 1.0F) {
                IAttributeInstance speedAttribute = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                double vanillaSpeed = 0.1D;
                speedAttribute.setBaseValue(Math.min((double) vanillaSpeed * scale, 3.0D));
            }
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingJumpEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        // change jump distance based on scale with a bottom of 0.5D and top of 2.0D
        float scale = ShrunkenState.getScale();
        double jumpMultiplier = Math.min(Math.max((double) scale, 0.5D) * 1.5D, 2.0D);
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

        // Ignores the attack event if source is labeled "inWall"
        if (ShrunkenState.getScale() < 1.0F) {
            event.setCanceled(true);
        }
    }

}
