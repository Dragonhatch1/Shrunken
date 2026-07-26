package com.xyrth.shrunken.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerRenderHandler {

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Pre event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;
        Minecraft mc = Minecraft.getMinecraft();
        float scale = ShrunkenState.getScale();
        float verticalOffset = getRidingOffset(player);

        // Don't Shrink or move if we are in the Inventory Screen so we don't bother the PaperDoll
        boolean isInventoryPreview = mc.currentScreen instanceof GuiInventory
            || mc.currentScreen instanceof GuiContainerCreative;

        GL11.glPushMatrix();
        if (!isInventoryPreview) {
            if (scale < 1.0F) {

                // adjust y-offset based on what im riding to account for boats, minecarts, etc.
                if (verticalOffset != 0.0F) {
                    GL11.glTranslated(0.0, verticalOffset, 0.0);
                }
                GL11.glTranslated(event.x, event.y, event.z);
                GL11.glScalef(scale, scale, scale);
                GL11.glTranslated(-event.x, -event.y, -event.z);
            } else {
                GL11.glTranslated(event.x, event.y, event.z);
                GL11.glScalef(scale, scale, scale);
                GL11.glTranslated(-event.x, -event.y, -event.z);
            }
        }
    }

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Post event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        GL11.glPopMatrix();
    }

    private float getRidingOffset(EntityPlayer player) {
        Entity ride = player.ridingEntity;

        // if we aren't riding anything, no offset. Otherwise, 0.5F Offset to our render.
        if (ride == null) {
            return 0.0F;
        }
        return 0.5F;
    }
}
