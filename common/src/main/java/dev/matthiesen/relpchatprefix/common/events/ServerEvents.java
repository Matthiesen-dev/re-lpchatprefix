package dev.matthiesen.relpchatprefix.common.events;

import dev.matthiesen.matthiesen_core.common.api.events.server.ServerEvent;
import dev.matthiesen.matthiesen_core.common.utility.chat.ServerMessagingUtil;
import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import dev.matthiesen.relpchatprefix.common.config.ChatPrefixConfig;
import dev.matthiesen.relpchatprefix.common.util.Formatter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class ServerEvents {
    public static boolean onServerChat(ServerEvent.Chat event) {
        ServerPlayer player = event.player();
        String rawText = event.message();
        try {
            String messageFormat = Formatter.getChatComponent(player, ChatPrefixConfig.SERVER_CONFIG.messageFormat.get());
            Component messageComponent = Formatter.processUserMessage(rawText, ChatPrefixConfig.SERVER_CONFIG.messageColor.get());
            Component finalComponent = Formatter.getMessageComponent(messageFormat, messageComponent);
            ServerMessagingUtil.sendToAllAndConsole(finalComponent);
            return true;
        } catch (RuntimeException e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Error handling server chat event for " + player.getName().getString(), e);
            return false;
        }
    }
}
