package com.xyrth.shrunken.mixin;

import com.xyrth.shrunken.client.ShrunkenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Redirect(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityClientPlayerMP;isEntityInsideOpaqueBlock()Z"))
    private boolean shrunken$suppressForcedFirstPerson(EntityClientPlayerMP player){
        if (ShrunkenState.getScale() < 1.0F ) {
            return false;
        }
        return player.isEntityInsideOpaqueBlock();
    }
}
