package com.xyrth.shrunken.mixin;

import com.xyrth.shrunken.client.ShrunkenState;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @ModifyArgs(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/AxisAlignedBB;expand(DDD)Lnet/minecraft/util/AxisAlignedBB;"))
    private void shrunken$scalePickupRadius(Args args){
        float scale = ShrunkenState.getScale();
        if (scale <= 1.0F) return;

        float radiusMultiplier = Math.min(scale * 0.2F, 5.0F);
        float radiusY = Math.min(scale * 0.2F, 2.0F);

        args.set(0, (double) args.get(0) * radiusMultiplier);
        args.set(1, (double) args.get(1) + (double) radiusY);
        args.set(2, (double) args.get(2) * radiusMultiplier);
    }
}
