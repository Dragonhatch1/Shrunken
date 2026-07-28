package com.xyrth.shrunken.mixin;

import com.xyrth.shrunken.client.ShrunkenState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

    @Inject(method = "getBlockReachDistance", at = @At("RETURN"), cancellable = true)
    private void shrunken$scaleReach(CallbackInfoReturnable<Float> cir){
        float scale = ShrunkenState.getScale();
        if (scale > 1.0F) {
            cir.setReturnValue(cir.getReturnValueF() * (scale * 0.5F));
        }
    }
}
