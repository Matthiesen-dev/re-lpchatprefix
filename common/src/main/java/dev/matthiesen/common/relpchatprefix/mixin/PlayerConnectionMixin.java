package dev.matthiesen.common.relpchatprefix.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(PlayerList.class)
public class PlayerConnectionMixin {
    @Inject(method = {"broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Z)V"}, at = {@At("HEAD")}, cancellable = true)
    public void broadcastSystemMessage(Component component, Function<ServerPlayer, Component> function, boolean bl, CallbackInfo ci) {
        String messageText = component.getString();
        if (messageText.contains("joined the game")) {
            ci.cancel();
        } else if (messageText.contains("left the game")) {
            ci.cancel();
        }
    }
}
