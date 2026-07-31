package dev.matthiesen.relpchatprefix.common.util;

import dev.matthiesen.matthiesen_core.common.core.permissions.LuckPermsHelper;
import dev.matthiesen.matthiesen_core.common.utility.chat.ChatUtils;
import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import dev.matthiesen.relpchatprefix.common.config.ModConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Formatter {
    public static @NotNull String getChatMessageFormat(ServerPlayer player, User user, String messageFormat) {
        CachedMetaData meta = user.getCachedData().getMetaData();

        String prefix = meta.getPrefix();
        String suffix = meta.getSuffix();

        ModConfig config = ReLPChatPrefix.INSTANCE.getConfig();

        if (!config.mainConfig.enablePrefix) prefix = null;
        if (!config.mainConfig.enableSuffix) suffix = null;

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        return getChatFormat(player, prefix, suffix, messageFormat);
    }

    public static @NotNull String getChatFormat(ServerPlayer player, String prefix, String suffix, String messageFormat) {
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
    }

    public static Component getMessageComponent(String playerMessage) {
        return getMessageComponent(playerMessage, null);
    }

    public static Component getMessageComponent(String playerMessage, @Nullable Component textAddition) {
        Component playerComponent = ReLPChatPrefix.INSTANCE.getTextParser().parse(playerMessage);
        if (textAddition == null) return playerComponent;
        return Component.empty().append(playerComponent).append(textAddition);
    }

    public static Component processUserMessage(String rawText, String messageColor) {
        ChatFormatting actualMessageColor = ChatUtils.getChatFormattingColor(messageColor);
        return Component.empty().append(" " + rawText)
                .withStyle(actualMessageColor);
    }

    public static String getChatComponent(ServerPlayer player, String messageFormat) {
        LuckPerms luckPerms = LuckPermsHelper.INSTANCE.getLuckPerms();

        if (luckPerms == null) {
            return null;
        }

        User user = LuckPermsHelper.INSTANCE.getUser(player.getUUID());
        if (user == null) {
            ReLPChatPrefix.INSTANCE.createWarnLog("User data not found for: " + player.getName().getString());
            return null;
        }

        return Formatter.getChatMessageFormat(player, user, messageFormat);
    }
}
