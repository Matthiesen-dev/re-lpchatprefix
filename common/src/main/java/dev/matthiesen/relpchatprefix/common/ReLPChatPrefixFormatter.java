package dev.matthiesen.relpchatprefix.common;

import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public final class ReLPChatPrefixFormatter {
    public static Map<String, String> getChatMessagePlaceholders(ServerPlayer player, User user) {
        try {
            CachedMetaData meta = user.getCachedData().getMetaData();

            String prefix = meta.getPrefix();
            String suffix = meta.getSuffix();
            String playerName = player.getName().getString();

            var config = ReLPChatPrefixConfig.SERVER_CONFIG;

            if (!config.enablePrefix.getAsBoolean()) prefix = null;
            if (!config.enableSuffix.getAsBoolean()) suffix = null;

            return getChatPlaceholders(prefix, playerName, suffix);
        } catch (Exception e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Failed to get chat message placeholders for player: " + player.getName().getString(), e);
            return getChatPlaceholders(null, null, null);
        }
    }

    private static boolean checkString(String string) {
        return string == null || string.isEmpty();
    }

    public static Map<String, String> getChatPlaceholders(
            String prefix,
            String playerName,
            String suffix
    ) {
        if (checkString(prefix)) prefix = "N/A";
        if (checkString(playerName)) playerName = "N/A";
        if (checkString(suffix)) suffix = "N/A";
        return Map.of(
                "{prefix}", prefix,
                "{player}", playerName,
                "{suffix}", suffix
        );
    }

    public static Component processUserMessage(String rawText, ChatFormatting messageColor) {
        try {
            return Component.empty().append(" " + rawText)
                    .withStyle(messageColor);
        } catch (Exception e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Failed to process user message: " + rawText, e);
            return Component.literal(rawText);
        }
    }
}
