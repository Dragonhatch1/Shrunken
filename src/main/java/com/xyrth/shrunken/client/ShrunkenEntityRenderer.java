package com.xyrth.shrunken.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

public class ShrunkenEntityRenderer extends EntityRenderer {

    private final Minecraft mc;
    private float offsetY;

    public ShrunkenEntityRenderer(Minecraft mc) {
        super(mc, mc.getResourceManager());
        this.mc = mc;
    }

    public void setOffset(float offset) {
        this.offsetY = offset;
    }

    public float getOffset() {
        return this.offsetY;
    }

    private boolean canShiftView() {
        return this.mc.thePlayer != null && !this.mc.thePlayer.isPlayerSleeping() && !this.mc.thePlayer.isRiding();
    }

    @Override
    public void updateCameraAndRender(float partialTicks) {
        if (this.canShiftView()) {
            this.mc.thePlayer.posY -= this.offsetY;
            this.mc.thePlayer.lastTickPosY -= this.offsetY;
            this.mc.thePlayer.prevPosY -= this.offsetY;

            float savedHeight = this.mc.thePlayer.eyeHeight;
            this.mc.thePlayer.eyeHeight = this.mc.thePlayer.getDefaultEyeHeight();

            super.updateCameraAndRender(partialTicks);

            this.mc.thePlayer.eyeHeight = savedHeight;
            this.mc.thePlayer.posY += this.offsetY;
            this.mc.thePlayer.lastTickPosY += this.offsetY;
            this.mc.thePlayer.prevPosY += this.offsetY;
        } else {
            super.updateCameraAndRender(partialTicks);
        }
    }

    @Override
    public void getMouseOver(float partialTicks) {
        if (this.canShiftView()) {
            this.mc.thePlayer.posY -= this.offsetY;
            this.mc.thePlayer.lastTickPosY -= this.offsetY;
            this.mc.thePlayer.prevPosY -= this.offsetY;

            float savedHeight = this.mc.thePlayer.eyeHeight;
            this.mc.thePlayer.eyeHeight = this.mc.thePlayer.getDefaultEyeHeight();

            super.getMouseOver(partialTicks);

            this.mc.thePlayer.eyeHeight = savedHeight;
            this.mc.thePlayer.posY += this.offsetY;
            this.mc.thePlayer.lastTickPosY += this.offsetY;
            this.mc.thePlayer.prevPosY += this.offsetY;
        } else {
            super.getMouseOver(partialTicks);
        }
    }
}
