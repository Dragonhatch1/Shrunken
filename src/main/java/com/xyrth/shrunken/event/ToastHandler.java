package com.xyrth.shrunken.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToastHandler {

    private String message = null;
    private long expireAtTick = -1;
    private boolean inZone = false;

    public void showToast(String msg, int durationTicks, boolean zone) {
        this.message = msg;
        this.expireAtTick = Minecraft.getMinecraft().theWorld.getTotalWorldTime() + durationTicks;
        this.inZone = zone;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (message == null) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld.getTotalWorldTime() > expireAtTick) {
            message = null; // expired, stop drawing it
            return;
        }

        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int screenWidth = res.getScaledWidth();

        int textWidth = mc.fontRenderer.getStringWidth(message);
        int x = screenWidth - textWidth - 10; // 10px padding from right edge
        int y = 10; // 10px from top

        if (inZone) {
            mc.fontRenderer.drawStringWithShadow(message, x, y, 0x55FF55);
        } else {
            mc.fontRenderer.drawStringWithShadow(message, x, y, 0xFF5555);
        }
    }
}
