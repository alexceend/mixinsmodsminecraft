package com.godyncend.mixinmodminecraft.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public class OnePlayerSleepMixin {

    @Inject(method = "isSleepingLongEnough", at = @At("HEAD"), cancellable = true)
    public void onePlayerSleep(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.isSleeping() && player.getSleepTimer() >= 50) {
            Objects.requireNonNull(player.getServer().getWorld(player.getEntityWorld().getRegistryKey())).setTimeOfDay(0);
            System.out.println("Se hizo de día !");
        }
    }
}
