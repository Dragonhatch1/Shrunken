package com.xyrth.shrunken.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.EntityRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.xyrth.shrunken.client.ShrunkenState;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    // Scales 3rd person distance to the scale of our Shrunken State. Brings camera in closer or farther.
    @ModifyVariable(method = "orientCamera", at = @At("STORE"), index = 10)
    private double shrunken$scaleThirdPersonDistance(double d7) {
        double scale = ShrunkenState.getScale();
        double thirdPersonScaled = d7;
        if (scale != 1.0F) {
            if (scale > 1.0F) {
                thirdPersonScaled = Math.min(d7 * scale, 30.0);
            } else {
                thirdPersonScaled = Math.max(d7 * scale, 3.0);
            }
        }
        return thirdPersonScaled;
    }

    @ModifyExpressionValue(method = "getMouseOver", at = @At(value = "CONSTANT", args= "doubleValue=3.0D"))
    private double shrunken$scaleSurvivalEntityReach(double origin){
        float scale = ShrunkenState.getScale();
        if (scale > 1.0F){
            return origin * ((double) scale * 0.7D);
        }
        return origin;
    }

    @ModifyExpressionValue(method = "getMouseOver", at = @At(value = "CONSTANT", args= "doubleValue=6.0D"))
    private double shrunken$scaleCreativeEntityReach(double origin){
        float scale = ShrunkenState.getScale();
        if (scale > 1.0F){
            return origin * ((double) scale * 0.7D);
        }
        return origin;
    }
}
