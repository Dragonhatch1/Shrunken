package com.xyrth.shrunken.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.xyrth.shrunken.client.ShrunkenState;

@Mixin(Entity.class)
public class MixinEntity {

    // Was having issues when using setSize in PlayerSizeHandler that made me surface on all blocks of water, even underwater.
    // This fixed it by shrinking the bounding box in proportion to my scale.
    @Redirect(
        method = "handleWaterMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/AxisAlignedBB;expand(DDD)Lnet/minecraft/util/AxisAlignedBB;"))
    private AxisAlignedBB shrunken$scaleWaterCheckExpand(AxisAlignedBB box, double x, double y, double z) {
        if ((Object) this instanceof EntityPlayer) {
            float scale = ShrunkenState.getScale();
            if (scale < 1.0F) {
                y *= scale;
            }
        }
        return box.expand(x, y, z);
    }
}
