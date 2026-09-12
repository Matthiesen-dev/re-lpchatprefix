package dev.matthiesen.relpchatprefix.common;

import dev.matthiesen.matthiesen_core.common.core.permissions.LuckPermsHelper;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReLPChatPrefixFormatter {
    public static @NotNull String getChatMessageFormat(ServerPlayer player, User user, String messageFormat) {
        try {
            CachedMetaData meta = user.getCachedData().getMetaData();

            String prefix = meta.getPrefix();
            String suffix = meta.getSuffix();

            var config = ReLPChatPrefixConfig.SERVER_CONFIG;

            if (!config.enablePrefix.getAsBoolean()) prefix = null;
            if (!config.enableSuffix.getAsBoolean()) suffix = null;

            if (prefix == null) prefix = "";
            if (suffix == null) suffix = "";

            return getChatFormat(player, prefix, suffix, messageFormat);
        } catch (Exception e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Failed to get chat message format for player: " + player.getName().getString(), e);
            return messageFormat;
        }
    }

    public static @NotNull String getChatFormat(ServerPlayer player, String prefix, String suffix, String messageFormat) {
        try {
            String playerName = player.getName().getString();

            if (messageFormat.contains("{prefix}")) {
                messageFormat = messageFormat.replace("{prefix}", prefix);
            }

            if (messageFormat.contains("{player}")) {
                messageFormat = messageFormat.replace("{player}", playerName);
            }

            if (messageFormat.contains("{suffix}")) {
                messageFormat = messageFormat.replace("{suffix}", suffix);
            }

            return messageFormat;
        } catch (Exception e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Failed to format chat message for player: " + player.getName().getString(), e);
            return messageFormat;
        }
    }

    public static Component getMessageComponent(String playerMessage) {
        return getMessageComponent(playerMessage, null);
    }

    public static Component getMessageComponent(String playerMessage, @Nullable Component textAddition) {
        try {
            Component playerComponent = ReLPChatPrefix.INSTANCE.getTextParser().parse(playerMessage);
            if (textAddition == null) return playerComponent;
            return Component.empty().append(playerComponent).append(textAddition);
        } catch (Exception e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Failed to parse player message: " + playerMessage, e);
            return Component.literal(playerMessage);
        }
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

    public static String getChatComponent(ServerPlayer player, String messageFormat) {
        try {
            LuckPerms luckPerms = LuckPermsHelper.INSTANCE.getLuckPerms();

            if (luckPerms == null) {
                return null;
            }

            User user = LuckPermsHelper.INSTANCE.getUser(player.getUUID());
            if (user == null) {
                ReLPChatPrefix.INSTANCE.createWarnLog("User data not found for: " + player.getName().getString());
                return null;
            }

            return ReLPChatPrefixFormatter.getChatMessageFormat(player, user, messageFormat);
        } catch (Exception e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Failed to get chat component for player: " + player.getName().getString() + ". This could be caused by an issue with LuckPerms or the user's data.", e);
            return null;
        }
    }
}
