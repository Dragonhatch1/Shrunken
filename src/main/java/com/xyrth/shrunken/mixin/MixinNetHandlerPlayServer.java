package com.xyrth.shrunken.mixin;

import net.minecraft.network.NetHandlerPlayServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.xyrth.shrunken.client.ShrunkenState;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {

    @ModifyExpressionValue(method = "processPlayer", at = @At(value = "CONSTANT", args = "doubleValue=1.65D"))
    private double shrunken$widenUpperBound(double origin) {
        float scale = ShrunkenState.getScale();
        if (scale <= 1.0F) return origin;

        float finalYOffset = (1.8F * scale) - 0.18F;
        return Math.max(origin, finalYOffset + 0.5F);
    }

    @ModifyExpressionValue(method = "processUseEntity", at = @At(value = "CONSTANT", args = "doubleValue=36.0D"))
    private double shrunken$scaleCreativeAttack(double origin) {
        float scale = ShrunkenState.getScale();

        if (scale > 1.0F) {
            double linearReach = 6.0D * ((double) scale * 0.55D); // Creative Attack Reach 6.0D
            return linearReach * linearReach;
        }
        return origin;
    }

    @ModifyExpressionValue(method = "processUseEntity", at = @At(value = "CONSTANT", args = "doubleValue=9.0D"))
    private double shrunken$scaleSurvivalAttack(double origin) {
        float scale = ShrunkenState.getScale();

        if (scale > 1.0F) {
            double linearReach = 3.0D * ((double) scale * 0.55D); // Survival Attack Reach 3.0D
            return linearReach * linearReach;
        }
        return origin;
    }

}
