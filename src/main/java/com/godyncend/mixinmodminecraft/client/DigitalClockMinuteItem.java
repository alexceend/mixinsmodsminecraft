package com.godyncend.mixinmodminecraft.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class DigitalClockMinuteItem extends Item {
    public DigitalClockMinuteItem(Settings settings) {
        super(settings);
    }
    public void updateTime(ItemStack stack, int minutes){
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("CustomModelData", minutes + 1);
    }
}
