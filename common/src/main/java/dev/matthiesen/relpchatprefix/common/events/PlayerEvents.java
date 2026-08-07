package dev.matthiesen.relpchatprefix.common.events;

import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import dev.matthiesen.matthiesen_core.common.utility.chat.ServerMessagingUtil;
import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import dev.matthiesen.relpchatprefix.common.config.ChatPrefixConfig;
import dev.matthiesen.relpchatprefix.common.data.PlayerStore;
import dev.matthiesen.relpchatprefix.common.util.Formatter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerEvents {
    public static void onPlayerJoin(PlayerEvent.Join event) {
        ServerPlayer player = event.player();
        loginLogoutEvent(player, ChatPrefixConfig.SERVER_CONFIG.chatOverrides_joinMessage.get());

        try {
            PlayerStore store = PlayerStore.getPlayerStore();
            String playerUUID = player.getStringUUID();

            if (!store.hasBeenSeen(playerUUID)) {
                store.setSeen(playerUUID);
                if (!ChatPrefixConfig.SERVER_CONFIG.firstJoin_enable.getAsBoolean()) return;
                String loginFormat = Formatter.getChatComponent(player, ChatPrefixConfig.SERVER_CONFIG.firstJoin_message.get());
                Component message = Formatter.getMessageComponent(loginFormat);
                ServerMessagingUtil.sendToAllAndConsole(message);
            }
        } catch (RuntimeException e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Error handling player join event for " + player.getName().getString(), e);
        }
    }

    public static void onPlayerLeave(PlayerEvent.Leave event) {
        ServerPlayer player = event.player();
        loginLogoutEvent(player, ChatPrefixConfig.SERVER_CONFIG.chatOverrides_leaveMessage.get());
    }

    private static void loginLogoutEvent(ServerPlayer player, String messageFormat) {
        try {
            messageFormat = Formatter.getChatComponent(player, messageFormat);
            Component message = Formatter.getMessageComponent(messageFormat);
            ServerMessagingUtil.sendToAllAndConsole(message);
        } catch (RuntimeException e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Error handling player login/logout event for " + player.getName().getString(), e);
        }
    }
}
