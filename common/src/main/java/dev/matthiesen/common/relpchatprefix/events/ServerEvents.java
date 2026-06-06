package dev.matthiesen.common.relpchatprefix.events;

import dev.matthiesen.common.matthiesen_lib_api.utility.ServerMessagingUtil;
import dev.matthiesen.common.relpchatprefix.Constants;
import dev.matthiesen.common.relpchatprefix.ReLPChatPrefix;
import dev.matthiesen.common.relpchatprefix.config.ModConfig;
import dev.matthiesen.common.relpchatprefix.util.Formatter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ServerEvents {
    public static void onServerChat(ServerPlayer player, String rawText) {
        try {
            ModConfig config = ReLPChatPrefix.getConfig();
            String messageFormat = Formatter.getChatComponent(player, config.mainConfig.messageFormat);
            Component messageComponent = Formatter.processUserMessage(rawText, config.mainConfig.messageColor);
            Component finalComponent = Formatter.getMessageComponent(messageFormat, messageComponent);
            ServerMessagingUtil.sendToAllAndConsole(finalComponent);
        } catch (RuntimeException e) {
            Constants.createErrorLog("Error handling server chat event for " + player.getName().getString(), e);
        }
    }
}
