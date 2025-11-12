package com.godyncend.mixinmodminecraft.mixin;

import com.godyncend.mixinmodminecraft.command.VanishCommand;
import com.godyncend.mixinmodminecraft.mixininterface.IGameMessageS2CPacket;
import com.godyncend.mixinmodminecraft.mixininterface.IPlayerListS2CPacket;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayerNetworkHandler {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> callback, CallbackInfo ci) {
        if (packet instanceof GameMessageS2CPacket) {
            if (shouldStopLeaveJoinMessage(packet)) ci.cancel();
        }

        if (packet instanceof PlayerListS2CPacket) {
            removeVanishedPlayers(packet);
        }
    }
    @Unique
    private boolean shouldStopLeaveJoinMessage(Packet<?> packet) {
        if (!(packet instanceof GameMessageS2CPacket)) return false;
        GameMessageS2CPacket chatPacket = (GameMessageS2CPacket) packet;

        // Only cancel join/leave messages, not normal chat
        Text message = ((IGameMessageS2CPacket) chatPacket).getMessageOnServer();
        if (message instanceof TranslatableText translatableText) {
            String key = translatableText.getKey();
            if (key.equalsIgnoreCase("multiplayer.player.joined") || key.equalsIgnoreCase("multiplayer.player.left")) {
                String messageString = translatableText.getString();
                for (PlayerEntity vanished : VanishCommand.vanished) {
                    if (messageString.contains(vanished.getName().getString())) {
                        return true; // cancel only vanish join/leave
                    }
                }
            }
        }

        return false; // do NOT cancel normal chat
    }

    @Unique
    private void removeVanishedPlayers(Packet<?> packet) {
        //if (VanishCommand.vanished.stream().anyMatch(vanished -> vanished.getUuid().equals(player.getUuid()))) return;

        IPlayerListS2CPacket playerListS2CPacket = (IPlayerListS2CPacket) packet;
        PlayerListS2CPacket.Action action = playerListS2CPacket.getActionOnServer();

        if (action.equals(PlayerListS2CPacket.Action.REMOVE_PLAYER) || action.equals(PlayerListS2CPacket.Action.UPDATE_LATENCY) || action.equals(PlayerListS2CPacket.Action.UPDATE_GAME_MODE))
            return;

        playerListS2CPacket.getEntriesOnServer().forEach(e -> System.out.println(e.getProfile().getId()));
        VanishCommand.vanished.forEach(e -> System.out.println(e.getUuid()));


        playerListS2CPacket.getEntriesOnServer().removeIf(entry -> VanishCommand.vanished.stream().anyMatch(vanished -> entry.getProfile().getId().equals(vanished.getUuid())));
    }

}
