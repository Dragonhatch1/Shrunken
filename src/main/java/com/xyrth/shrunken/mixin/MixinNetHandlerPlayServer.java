package com.xyrth.shrunken.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.xyrth.shrunken.client.ShrunkenState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {

    @Shadow
    public EntityPlayerMP playerEntity;

    @ModifyExpressionValue(method = "processPlayer", at = @At(value = "CONSTANT", args = "doubleValue=1.65D"))
    private double shrunken$widenUpperBound(double origin) {
        float scale = ShrunkenState.getScale();
        if (scale <= 1.0F) return origin;

        float finalYOffset = (1.8F * scale) - 0.18F;
        return Math.max(origin, finalYOffset + 0.5F);
    }
}
