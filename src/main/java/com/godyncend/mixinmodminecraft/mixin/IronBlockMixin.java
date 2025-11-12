package com.godyncend.mixinmodminecraft.mixin;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class IronBlockMixin {
    @Shadow
    @Nullable
    protected abstract Inventory getOutputInventory();


    @Shadow
    private DefaultedList<ItemStack> inventory;

    @Inject(method = "transfer", at = @At("TAIL"), cancellable = true)
    private static void assertChestTransformsIronToBlocks(@Nullable Inventory from, Inventory to, ItemStack stack, @Nullable Direction side, CallbackInfoReturnable<ItemStack> cir) {
        try {
            to.size();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (to.size() == 27 || to.size() == 54) {
            for (int i = 0; i < to.size(); i++) {
                if (to.getStack(i).getItem().equals(Items.IRON_INGOT)) {
                    int number_iron_blocks = to.getStack(i).getCount() / 9;
                    ItemStack new_stack = new ItemStack(Items.IRON_BLOCK, number_iron_blocks);
                    if (number_iron_blocks != 0) {
                        boolean wereAdded = add(to, new_stack);
                        if (wereAdded) {
                            to.setStack(i, new ItemStack(Items.IRON_INGOT, to.getStack(i).getCount() - number_iron_blocks * 9));
                            to.markDirty();
                        }
                    }
                }
            }
        }

    }

    private static boolean add(Inventory inventory, ItemStack itemStack) {
        try {
            if (inventory == null || itemStack == null) return false;
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                if (stack == null) continue;
                if (stack.isEmpty()) {
                    inventory.setStack(i, itemStack);
                    inventory.markDirty();
                    return true;
                } else if (stack.getName().equals(itemStack.getName())
                        && stack.getCount() + itemStack.getCount() <= 64) {
                    ItemStack newStack = new ItemStack(itemStack.getItem(), itemStack.getCount() + inventory.getStack(i).getCount());
                    inventory.setStack(i, newStack);
                    inventory.markDirty();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
