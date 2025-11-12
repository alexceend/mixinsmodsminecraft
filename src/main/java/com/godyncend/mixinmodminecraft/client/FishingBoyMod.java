package com.godyncend.mixinmodminecraft.client;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FishingBoyMod implements ModInitializer {

    public static final Item CUSTOM_IMAGE_ITEM = new CustomImageItem(new FabricItemSettings().group(ItemGroup.MISC));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("ironblocksmod", "fishing_item"), CUSTOM_IMAGE_ITEM);
    }
}
