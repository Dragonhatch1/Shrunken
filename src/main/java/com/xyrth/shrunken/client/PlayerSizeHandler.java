package com.xyrth.shrunken.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class PlayerSizeHandler {

    private static float scale = ShrunkenState.getScale();
    private static float offset = ShrunkenState.getEyeOffset();
    private static boolean rendererSwapped = false;
    private static Method methodSetPlayerSize;
    private static float defaultWidth = 0.6F;
    private static float defaultHeight = 1.8F;
    private static float scaledStepHeight = 0.5F * scale;

    @SubscribeEvent
    public void onLivingRender(Pre event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.entity;
        // TODO Fix Boat Placement
        // TODO Fix Minecart Placement
        // TODO Fix Sitting Position so we can sit in Breakroom
        // Forces light to be calculated based off of our True Y Position with offset.
        double trueY = player.posY + offset;
        int light = player.worldObj.getLightBrightnessForSkyBlocks(
            MathHelper.floor_double(player.posX),
            MathHelper.floor_double(trueY),
            MathHelper.floor_double(player.posZ),
            0);
        int lightmapX = light % 65536;
        int lightmapY = light / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lightmapX, (float) lightmapY);

        GL11.glPushMatrix();
        GL11.glTranslated(0.0, offset, 0.0);
        GL11.glTranslated(event.x, event.y, event.z);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(-event.x, -event.y, -event.z);
    }

    @SubscribeEvent
    public void onLivingRender(Post event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) throws InvocationTargetException, IllegalAccessException {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;

        methodSetPlayerSize = ReflectionHelper.findMethod(
            Entity.class,
            player,
            new String[] { "setSize", "func_70105_a", "a" },
            new Class[] { Float.TYPE, Float.TYPE });

        methodSetPlayerSize.invoke(player, (defaultWidth * scale), (defaultHeight * scale));

        //Change stepHeight if its not the scaled step height
        if (player.stepHeight != scaledStepHeight) {
            player.stepHeight = scaledStepHeight;
        }

        //Change Player Speed if Scale is above 1
        if (!player.worldObj.isRemote) {
            if (scale > 1.0F) {
                IAttributeInstance speedAttribute = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                double vanillaSpeed = 0.1D;
                speedAttribute.setBaseValue(Math.min((double) vanillaSpeed * scale, 3.0D));
            }
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingJumpEvent event){
        if (!(event.entity instanceof EntityPlayer)) return;

        //change jump distance based on scale with a bottom of 0.5D and top of 2.0D
        float scale = ShrunkenState.getScale();
        double jumpMultiplier = Math.min(Math.max((double)scale, 0.5D) * 1.5D, 2.0);
        event.entityLiving.motionY *= jumpMultiplier;
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
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

        //Ignores the attack event if source is labeled "inWall"
        if (ShrunkenState.getScale() < 1.0F) {
            event.setCanceled(true);
        }
    }

}
