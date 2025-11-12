package com.godyncend.mixinmodminecraft.mixin;

import com.godyncend.mixinmodminecraft.mixininterface.IGameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameMessageS2CPacket.class)
public class GameMessageS2CPacketMixin implements IGameMessageS2CPacket {
    @Shadow
    private Text message;

    public Text getMessageOnServer() {
        return message;
    }
}
