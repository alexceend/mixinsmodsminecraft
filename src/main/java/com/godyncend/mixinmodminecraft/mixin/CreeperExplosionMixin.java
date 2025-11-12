package com.godyncend.mixinmodminecraft.mixin;

import com.godyncend.mixinmodminecraft.utils.QuoteFetcher;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public class CreeperExplosionMixin {
    @Inject(method = "explode", at = @At("TAIL"))
    private void afterCreeperExplodesShowQuote(CallbackInfo ci) {
        CreeperEntity creeper = (CreeperEntity) (Object) this;

        if(!creeper.world.isClient){
            ServerWorld world = (ServerWorld) creeper.world;
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (player.squaredDistanceTo(creeper) < 100 * 100) {
                    QuoteFetcher.fetchAndSendToPlayerScreen(player);
                }
            }
        }
    }
}
