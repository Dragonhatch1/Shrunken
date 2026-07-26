package com.xyrth.shrunken.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.xyrth.shrunken.Shrunken;
import com.xyrth.shrunken.network.PacketSyncScale;
import com.xyrth.shrunken.util.Config;

import alkalus.main.mixins.hooks.EntitySizeManager.OffsetContents;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class PlayerSizeHandler {

    private static Method methodSetPlayerSize;

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) throws InvocationTargetException, IllegalAccessException {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;

        float scale = ShrunkenState.getScale();
        float scaledStepHeight = 0.5F * scale; // default step height is 0.5F
        float scaledHeight = 1.8F * scale; // default height is 1.8F
        float scaledWidth = 0.6F * scale; // default width is 0.6F
        boolean SERVER = !player.worldObj.isRemote;
        boolean CLIENT = player.worldObj.isRemote;

        // Sets Player Size & Step Height on Client | Sets Player Size, Step Height, & Eye Height on Server
        if (CLIENT || player.ticksExisted % 20 == 0) {
            float currentHeight = player.height;
            if (scaledHeight != currentHeight) {
                if (methodSetPlayerSize == null) {
                    methodSetPlayerSize = ReflectionHelper.findMethod(
                        Entity.class,
                        player,
                        new String[] { "setSize", "func_70105_a", "a" },
                        new Class[] { Float.TYPE, Float.TYPE });
                }

                player.stepHeight = scaledStepHeight;
                methodSetPlayerSize.invoke(player, (scaledWidth), (scaledHeight));
                if (SERVER) {
                    player.eyeHeight = scaledHeight * 0.92F;
                }
            }
        }

        // Sets our Y-Offset using Witchery Extras Mixin
        if (CLIENT) {
            OffsetContents contents = OffsetContents.get(player);
            if (contents != null) {
                contents.targetOffset = 1.8F * (1.0F - scale);
            }
        }

        // Change Player Speed if Scale is above 1
        if (SERVER) {
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
        float scale = ShrunkenState.getScale();

        // change jump distance based on scale with a bottom of 0.5D and top of 2.0D
        double jumpMultiplier = Math.min(Math.max((double) scale, 0.5D) * 1.5D, 2.0D);
        event.entityLiving.motionY *= jumpMultiplier;
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;

        Entity ride = player.ridingEntity;
        boolean isBoat = (ride != null && ride.getClass()
            .getSimpleName()
            .toLowerCase()
            .contains("boat"));

        // ignores drowning damage if in boat
        if ("drown".equals(event.source.getDamageType()) && isBoat) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;

        Entity ride = player.ridingEntity;
        boolean isBoat = (ride != null && ride.getClass()
            .getSimpleName()
            .toLowerCase()
            .contains("boat"));

        // ignores drowning attack if in boat
        if ("drown".equals(event.source.getDamageType()) && isBoat) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) return;
        if (event.player.worldObj.isRemote) return;

        Shrunken.NETWORK.sendTo(new PacketSyncScale(Config.scale), (EntityPlayerMP) event.player);

    }

}
