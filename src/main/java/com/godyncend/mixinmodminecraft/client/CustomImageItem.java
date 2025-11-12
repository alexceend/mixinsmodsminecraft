package com.godyncend.mixinmodminecraft.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class CustomImageItem extends Item {
    public CustomImageItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient) {
            // Code to place the item frame and set the custom image
            // Create an item frame entity and set the custom texture
            // This might involve custom code to associate your image with the item frame

            // Example: Replace this with actual logic to create the item frame
            // ItemFrameEntity itemFrame = new ItemFrameEntity(world, pos, direction);
            // itemFrame.setImage(new Identifier(MOD_ID, "textures/my_image.png"));
            // world.spawnEntity(itemFrame);
        }
        return ActionResult.success(world.isClient);
    }
}
