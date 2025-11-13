package com.godyncend.mixinmodminecraft.client;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClockClientMod implements ModInitializer {
    public static final Item DIGITAL_CLOCK_ITEM = new DigitalClockHourItem(
            new FabricItemSettings().group(ItemGroup.MISC)
    );
    @Override
    public void onInitialize() {
        System.out.println("[CLOCK CLIENT] INITIALIZED");
        ClockUpdater.init();
        Registry.register(Registry.ITEM, new Identifier("ironblocksmod", "digital_clock_hour"), DIGITAL_CLOCK_ITEM);
    }
}
