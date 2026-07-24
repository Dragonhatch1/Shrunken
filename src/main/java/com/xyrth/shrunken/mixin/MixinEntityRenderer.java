package com.xyrth.shrunken.mixin;

import net.minecraft.client.renderer.EntityRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.xyrth.shrunken.client.ShrunkenState;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    //Scales 3rd person distance to the scale of our Shrunken State. Brings camera in closer or farther.
    @ModifyVariable(method = "orientCamera", at = @At("STORE"), index = 10)
    private double shrunken$scaleThirdPersonDistance(double d7) {
        return d7 * ShrunkenState.getScale();
    }
}
