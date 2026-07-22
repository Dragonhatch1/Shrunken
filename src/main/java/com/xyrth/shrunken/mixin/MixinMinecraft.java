package com.xyrth.shrunken.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.xyrth.shrunken.client.ShrunkenState;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Redirect(
        method = "runGameLoop",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;isEntityInsideOpaqueBlock()Z"))
    private boolean shrunken$suppressForcedFirstPerson(EntityClientPlayerMP player) {
        if (ShrunkenState.getScale() < 1.0F) {
            return false;
        }
        return player.isEntityInsideOpaqueBlock();
    }
}
