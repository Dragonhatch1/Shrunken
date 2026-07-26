package com.xyrth.shrunken.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerRenderHandler {

    private static float scale = ShrunkenState.getScale();

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Pre event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;
        Minecraft mc = Minecraft.getMinecraft();

        //Don't Shrink or move if we are in the Inventory Screen so we don't bother the PaperDoll
        boolean isInventoryPreview = mc.currentScreen instanceof GuiInventory
            || mc.currentScreen instanceof GuiContainerCreative;

        GL11.glPushMatrix();
        if (!isInventoryPreview) {
            // adjust y-offset based on what im riding to account for boats, minecarts, etc.
            float verticalOffset = getRidingOffset(player);

            if (verticalOffset != 0.0F) {
                GL11.glTranslated(0.0, verticalOffset, 0.0);
            }
            GL11.glTranslated(event.x, event.y, event.z);
            GL11.glScalef(scale, scale, scale);
            GL11.glTranslated(-event.x, -event.y, -event.z);
        }
    }

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Post event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        GL11.glPopMatrix();
    }

    private float getRidingOffset(EntityPlayer player) {
        Entity ride = player.ridingEntity;

        //if we aren't riding anything, no offset. Otherwise, 0.5F Offset to our render.
        if (ride == null) {
            return 0.0F;
        }
        return 0.5F;
    }
}
