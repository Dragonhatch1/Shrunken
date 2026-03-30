package com.xyrth.shrunken.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PermanentPotionHandler {

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;

        // Only run server-side to avoid double application
        if (player.worldObj.isRemote) return;

        // Reapply when effect is missing or about to expire (< 10 ticks)
        PotionEffect current = player.getActivePotionEffect(Potion.potionTypes[55]);
        if (current == null || current.getDuration() < 10) {
            player.addPotionEffect(
                new PotionEffect(
                    55, // Effect ID
                    72000, // Duration in ticks (10 seconds)
                    0, // Amplifier (0 = level I)
                    false // isAmbient (false = show particles)
                ));
        }
    }
}
