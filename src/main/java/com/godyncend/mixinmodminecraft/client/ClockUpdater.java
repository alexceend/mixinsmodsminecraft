package com.godyncend.mixinmodminecraft.client;

import com.godyncend.mixinmodminecraft.utils.ClockTime;
import com.godyncend.mixinmodminecraft.utils.TimeFetcher;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class ClockUpdater {
    private static long lastFetchTime = 0;
    private static long FETCH_INTERVAL_MS = 10_000;

    public static void init(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long now = System.currentTimeMillis();
            if (now - lastFetchTime > FETCH_INTERVAL_MS) {
                lastFetchTime = now;
                TimeFetcher.fetchTime().thenAccept(hourRequest -> {
                    ClockTime.currentHour = hourRequest.getHour();
                    System.out.println("[CLOCK REQUEST] GOT TIME " + hourRequest.getHour());
                    ClockTime.currentMinute = hourRequest.getMinute();
                    updateHeldItems(client);
                    updateItemFrames(client.world);
                    System.out.println("[CLOCK UPDATER] UPDATED HELD ITEMS " + ClockTime.currentHour + ":" + ClockTime.currentMinute);
                });
            }
        });
    }

    private static void updateItemFrames(World world){
        if(world == null){
            System.out.println("[CLOCK UPDATER] NO WORLD FOUND");
            return;
        }

        System.out.println("[CLOCK UPDATER] WORLD FOUND");


        for (Entity e : world.getEntitiesByClass(ItemFrameEntity.class, new Box(-30000000, -64, -30000000, 30000000, 320, 30000000),
                p -> true)) {

            if (!(e instanceof ItemFrameEntity)) continue;

            ItemFrameEntity frame = (ItemFrameEntity) e;
            ItemStack stack = frame.getHeldItemStack();

            if (stack.getItem() instanceof DigitalClockHourItem) {
                // Copy the stack to trigger the model update
                ItemStack newStack = stack.copy();
                ((DigitalClockHourItem) newStack.getItem()).updateTime(newStack, ClockTime.currentHour);
                frame.setHeldItemStack(newStack, false); // false = don't drop old stack
            }
        }
    }


    private static void updateHeldItems(MinecraftClient client) {
        if (client.player == null) {
            System.out.println("[ClockUpdater] Player not ready yet");
            return;
        }

        // Update main hand
        var inventory = client.player.inventory;

        // Main hand
        int selectedSlot = inventory.selectedSlot;
        ItemStack mainStack = inventory.getStack(selectedSlot);

        System.out.println("[ClockUpdater] Main hand item: " + mainStack.getItem().toString());
        if (mainStack.getItem() instanceof DigitalClockHourItem) {
            ItemStack newItemStack = mainStack.copy();
            ((DigitalClockHourItem) newItemStack.getItem()).updateTime(newItemStack, ClockTime.currentHour);
            inventory.setStack(selectedSlot, newItemStack);
            System.out.println("[ClockUpdater] Updated main hand clock to hour: " + ClockTime.currentHour);

        }

        // You can add logic for DigitalClockMinuteItem here similarly
    }
}
