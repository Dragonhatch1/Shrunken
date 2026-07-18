package com.xyrth.shrunken.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BreakroomHandler {

    // hardcoded Breakroom Boundaries. Will change to command later to change area
    private static final double MIN_X = 0, MAX_X = 10;
    private static final double MIN_Y = 0, MAX_Y = 250;
    private static final double MIN_Z = 0, MAX_Z = 10;


    private final Set<UUID> playersInBreakroom = new HashSet<>();

    // Values for Time Vial
    private static final String NBT_STORED_TICK = "storedTimeTick";
    private static final int NUMBER_ERR = -846280;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        if (player.worldObj.isRemote) return;

        // match the same 20 ticks as Not-Leisure Time Vial
        if (player.worldObj.getTotalWorldTime() % 20 != 0) return;

        if (!isInBreakroom(player)) return;

        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack == null) continue;
            if (!stack.getItem()
                .getClass()
                .getName()
                .equals("com.xir.NHUtilities.common.items.timeVial.TimeVial")) continue;

            NBTTagCompound tag = stack.getTagCompound();
            if (tag == null) continue;

            int stored = tag.getInteger(NBT_STORED_TICK);
            if (stored == NUMBER_ERR) continue;

            tag.setInteger(NBT_STORED_TICK, stored + 10);
            stack.setTagCompound(tag);
        }
    }

    private boolean isInBreakroom(EntityPlayer player) {
        return player.posX >= MIN_X && player.posX <= MAX_X
            && player.posY >= MIN_Y
            && player.posY <= MAX_Y
            && player.posZ >= MIN_Z
            && player.posZ <= MAX_Z;
    }
}
