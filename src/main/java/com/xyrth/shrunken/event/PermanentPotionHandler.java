package com.xyrth.shrunken.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.network.PacketSyncEntitySize;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PermanentPotionHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;

        // Only run server-side to avoid double application
        if (player.worldObj.isRemote) return;

        // Reapply when effect is missing or about to expire (< 10 ticks)
        PotionEffect current = player.getActivePotionEffect(Potion.potionTypes[77]);
        if (current == null || current.getDuration() < 10) {
            player.addPotionEffect(
                new PotionEffect(
                    77, // Effect ID
                    72000, // Duration in ticks (10 seconds)
                    0, // Amplifier (0 = level I)
                    true // isAmbient (false = show particles)
                ));
        }
        if (player.stepHeight < 0.2F) {
            player.stepHeight = 0.2F;
            Witchery.packetPipeline.sendToAll((IMessage) (new PacketSyncEntitySize(player)));
        }

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
        EntityPlayer player = event.entityPlayer;
        World world = event.entityPlayer.worldObj;

        if (event.entityPlayer.isPotionActive(Witchery.Potions.RESIZING)) {
            if (!world.isRemote) {
                event.result = null;
            }
        }
    }
}
