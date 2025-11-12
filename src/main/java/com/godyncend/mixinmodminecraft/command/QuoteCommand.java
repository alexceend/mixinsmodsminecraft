package com.godyncend.mixinmodminecraft.command;

import com.godyncend.mixinmodminecraft.utils.QuoteFetcher;
import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;

public class QuoteCommand implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated) -> {
                    dispatcher.register(CommandManager.literal("quote")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if(player != null) {
                                    QuoteFetcher.fetchAndSendToChat(player);
                                }
                                return Command.SINGLE_SUCCESS;
                            })
                    );
                }
        );
    }
}
