package com.xyrth.shrunken.mixin;

import net.minecraft.client.renderer.EntityRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.xyrth.shrunken.client.ShrunkenState;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    private static final float CAMERA_NUDGE = 0.15F;

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), index = 3)
    private float shrunken$nudgeCamera(float f1) {
        return f1 - CAMERA_NUDGE;
    }

    @ModifyVariable(method = "orientCamera", at = @At("STORE"), index = 10)
    private double shrunken$scaleThirdPersonDistance(double d7) {
        return d7 * ShrunkenState.getScale();
    }
}
