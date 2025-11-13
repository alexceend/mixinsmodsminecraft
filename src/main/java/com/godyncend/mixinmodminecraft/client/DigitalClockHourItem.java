package com.godyncend.mixinmodminecraft.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;


public class DigitalClockHourItem extends Item {
    public DigitalClockHourItem(Settings settings) {
        super(settings);
    }

    public void updateTime(ItemStack stack, int hour) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("CustomModelData", hour + 1);
    }


}
