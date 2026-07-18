package com.xyrth.shrunken.event;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.xyrth.shrunken.util.BreakroomConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class BreakroomHandler {

    private final Set<UUID> playersInBreakroom = new HashSet<>();
    private final ToastHandler toastHandler = new ToastHandler();

    // Values for Time Vial
    private static final String NBT_STORED_TICK = "storedTimeTick";
    private static final int NUMBER_ERR = -846280;

    public BreakroomHandler() {
        MinecraftForge.EVENT_BUS.register(toastHandler);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        if (player.worldObj.isRemote) return;

        boolean inZone = isInBreakroom(player);
        boolean wasInZone = playersInBreakroom.contains(player.getUniqueID());

        if (inZone && !wasInZone) {
            player.worldObj.playSoundAtEntity(player, "shrunken:breakroom_enter", 0.1F, 1.0F);
            toastHandler.showToast("Breakroom Entered", 100, true);
            playersInBreakroom.add(player.getUniqueID());
        } else if (!inZone && wasInZone) {
            player.worldObj.playSoundAtEntity(player, "shrunken:breakroom_leave", 0.1F, 1.0F);
            toastHandler.showToast("Breakroom Left", 100, false);
            playersInBreakroom.remove(player.getUniqueID());
        }

        if (inZone && player.worldObj.getTotalWorldTime() % 20 == 0) {

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
    }

    private boolean isInBreakroom(EntityPlayer player) {
        return MathHelper.floor_double(player.posX) >= BreakroomConfig.minX && MathHelper.floor_double(player.posX) <= BreakroomConfig.maxX
            && MathHelper.floor_double(player.posY) >= BreakroomConfig.minY
            && MathHelper.floor_double(player.posY) <= BreakroomConfig.maxY
            && MathHelper.floor_double(player.posZ) >= BreakroomConfig.minZ
            && MathHelper.floor_double(player.posZ) <= BreakroomConfig.maxZ;
    }
}
