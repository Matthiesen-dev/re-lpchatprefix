package dev.matthiesen.common.relpchatprefix.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({PlayerList.class, ServerGamePacketListenerImpl.class})
public abstract class DisableVanillaJoinLeaveMessagesMixin {
    @WrapWithCondition(
            method = {
                    "placeNewPlayer",
                    "removePlayerFromWorld"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"
            )
    )
    public boolean disableVanillaJoinAndLeaveMessage(PlayerList instance, Component component, boolean bl) {
        // Always returning false is bad and leads to other's changes being silently ignored, but is necessary here I guess...
        return false;
    }
}
