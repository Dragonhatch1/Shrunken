package com.xyrth.shrunken.event;

import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

    //hardcoded Breakroom Boundaries
    private static final double MIN_X = 0, MAX_X = 10;
    private static final double MIN_Y = 0, MAX_Y = 250;
    private static final double MIN_Z = 0, MAX_Z = 10;

    // Values for Time Vial
    private static final String NBT_STORED_TICK = "storedTimeTick";
    private static final int NUMBER_ERR = -846280;


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

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        if (player.worldObj.isRemote) return;

        //match the same 20 ticks as Not-Leisure Time Vial
        if (player.worldObj.getTotalWorldTime() % 20 != 0) return;

        if (!isInBreakroom(player)) return;

        for (ItemStack stack : player.inventory.mainInventory){
            if (stack == null) continue;
            if (!stack.getItem().getClass().getName().equals("com.xir.NHUtilities.common.items.timeVial.TimeVial")) continue;

            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) continue;

            int stored = tag.getInteger(NBT_STORED_TICK);
            if (stored == NUMBER_ERR) continue;

            tag.setInteger(NBT_STORED_TICK, stored + 20);
            stack.setTagCompound(tag);
        }
    }

    private boolean isInBreakroom(EntityPlayer player){
        return player.posX >= MIN_X && player.posX <= MAX_X
            && player.posY >= MIN_Y && player.posY <= MAX_Y
            && player.posZ >= MIN_Z && player.posZ <= MAX_Z;
    }
}
