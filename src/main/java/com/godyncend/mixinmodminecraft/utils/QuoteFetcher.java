package com.godyncend.mixinmodminecraft.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class QuoteFetcher {

    private static CompletableFuture<String> fetchQuote() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://dummyjson.com/quotes/random");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
                    // Works on all recent Gson versions
                    JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
                    String quote = json.get("quote").getAsString();
                    String author = json.get("author").getAsString();
                    return "§aQuote: §f" + quote + " §7— " + author;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "§cFailed to fetch quote.";
            }
        });
    }

    // Optional helper to send directly to chat
    public static void fetchAndSendToChat(ServerPlayerEntity player) {
        if (player == null) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        server.execute(() -> {
            player.sendMessage(Text.of("§7Fetching a quote..."), false);
        });

        fetchQuote().thenAccept(message -> {
            server.execute(() -> {
                player.sendMessage(Text.of(message), false);
            });
        });
    }

    public static void fetchAndSendToPlayerScreen(ServerPlayerEntity player) {
        if (player == null) return;

        MinecraftServer server = player.getServer();
        if (server == null) return;

        fetchQuote().thenAccept(message -> {
            server.execute(() -> {
                int midpoint = message.length() / 2;

                int splitIndex = message.lastIndexOf(' ', midpoint);
                if (splitIndex == -1) splitIndex = midpoint;

                String line1 = message.substring(0, splitIndex).trim();
                String line2 = message.substring(splitIndex).trim();

                // Title
                player.networkHandler.sendPacket(
                        new TitleS2CPacket(TitleS2CPacket.Action.TITLE, Text.of("§aQUOTE"))
                );
                // Subtitle
                player.networkHandler.sendPacket(
                        new TitleS2CPacket(TitleS2CPacket.Action.SUBTITLE, Text.of(line1))
                );

                // Send second line after 20 ticks (~1 second)
                player.networkHandler.sendPacket(
                        new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, Text.of(line2))
                );

                // Fade-in (10 ticks), stay (60 ticks), fade-out (10 ticks)
                player.networkHandler.sendPacket(
                        new TitleS2CPacket(10, 60, 10)
                );
            });
        });
    }

}
