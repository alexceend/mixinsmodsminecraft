package com.godyncend.mixinmodminecraft.mixin;

import com.godyncend.mixinmodminecraft.command.VanishCommand;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class PlayerEntityInvisibleMixin {
    @Shadow
    protected abstract void setFlag(int index, boolean value);

    @Shadow
    public abstract UUID getUuid();

    @Inject(method = "setInvisible", at = @At("TAIL"))
    public void setInvisibleMixin(boolean invisible, CallbackInfo ci) {
        this.setFlag(5, VanishCommand.vanished.stream().anyMatch(player -> player.getUuid().equals(this.getUuid())));
    }
}
