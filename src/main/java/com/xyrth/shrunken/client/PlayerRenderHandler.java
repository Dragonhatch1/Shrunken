package com.xyrth.shrunken.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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
import cpw.mods.fml.common.gameevent.TickEvent;

public class PlayerRenderHandler {

    private static float scale = ShrunkenState.getScale();
    private static float offset = ShrunkenState.getEyeOffset();
    private static boolean rendererSwapped = false;

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Pre event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;
        float verticalOffset = getRidingOffset(player);

        // Forces light to be calculated based off of our True Y Position with offset.
        double trueY = player.posY + offset;
        int sampleY = MathHelper.floor_double(trueY);
        int blockX = MathHelper.floor_double(player.posX);
        int blockZ = MathHelper.floor_double(player.posZ);
        int light;

        Block sampleBlock = player.worldObj.getBlock(blockX, sampleY, blockZ);

        if (sampleBlock.isOpaqueCube()){
            light = player.worldObj.getLightBrightnessForSkyBlocks(blockX, MathHelper.floor_double(player.posY), blockZ, 0);
        } else {
            light = player.worldObj.getLightBrightnessForSkyBlocks(blockX, sampleY, blockZ, 0);
        }

        int lightmapX = light % 65536;
        int lightmapY = light / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lightmapX, (float) lightmapY);

        GL11.glPushMatrix();
        if (verticalOffset != 0.0F) {
            GL11.glTranslated(0.0, verticalOffset, 0.0);
        }
        GL11.glTranslated(event.x, event.y, event.z);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(-event.x, -event.y, -event.z);
    }

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Post event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (rendererSwapped) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // hooks into our custom camera and Y Offsets
        ShrunkenEntityRenderer renderer = new ShrunkenEntityRenderer(mc);
        renderer.setOffset(offset);
        mc.entityRenderer = renderer;

        rendererSwapped = true;
    }

    private float getRidingOffset(EntityPlayer player) {
        Entity ride = player.ridingEntity;

        if (ride == null) {
            return player.isPlayerSleeping() ? 0.0F : offset;
        }
        //TODO Change to (ride != null) if testing goes fine. this many calls is stupid. looking for specific use
        // cases, but 0.5F seems to be nice.
        if (ride instanceof EntityBoat) {
            return 0.5F;
        }
        if (ride.getClass()
            .getSimpleName()
            .toLowerCase()
            .contains("boat")) {
            return 0.5F;
        }
        if (ride instanceof EntityMinecart) {
            return 0.5F;
        }
        if (ride instanceof EntityHorse) {
            return 0.5F;
        }
        return 0.0F;
    }
}
