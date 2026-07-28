package dev.matthiesen.relpchatprefix.common.events;

import dev.matthiesen.matthiesen_core.common.utility.chat.ServerMessagingUtil;
import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import dev.matthiesen.relpchatprefix.common.config.ModConfig;
import dev.matthiesen.relpchatprefix.common.util.Formatter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class ServerEvents {
    public static void onServerChat(ServerPlayer player, String rawText) {
        try {
            ModConfig config = ReLPChatPrefix.INSTANCE.getConfig();
            String messageFormat = Formatter.getChatComponent(player, config.mainConfig.messageFormat);
            Component messageComponent = Formatter.processUserMessage(rawText, config.mainConfig.messageColor);
            Component finalComponent = Formatter.getMessageComponent(messageFormat, messageComponent);
            ServerMessagingUtil.sendToAllAndConsole(finalComponent);
        } catch (RuntimeException e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Error handling server chat event for " + player.getName().getString(), e);
        }
    }
}
