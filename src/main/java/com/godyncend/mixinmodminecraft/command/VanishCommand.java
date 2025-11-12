package com.godyncend.mixinmodminecraft.command;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;

import java.util.HashSet;
import java.util.UUID;


public class VanishCommand implements ModInitializer {

    public static final HashSet<PlayerEntity> vanished = new HashSet<>();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated) -> {
                    dispatcher.register(CommandManager.literal("Vanish")
                            .executes(context -> {
                                PlayerEntity player = context.getSource().getPlayer();
                                if (vanished.contains(player)) {
                                    vanished.remove(player);
                                    player.setInvisible(false);
                                    System.out.println("Eliminado jugador " + player.getName() + " al vector de vanished");
                                } else {
                                    vanished.add(player);
                                    player.setInvisible(true);
                                    System.out.println("Añadido jugador " + player.getName() + " al vector de vanished");
                                }
                                return Command.SINGLE_SUCCESS;
                            })
                    );
                }
        );
    }
}
