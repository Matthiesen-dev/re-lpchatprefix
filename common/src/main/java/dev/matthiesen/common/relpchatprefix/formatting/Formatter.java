package dev.matthiesen.common.relpchatprefix.formatting;

import dev.matthiesen.common.relpchatprefix.Constants;
import dev.matthiesen.common.relpchatprefix.ReLPChatPrefix;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Formatter {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static NamedTextColor getAdventureColor(String color) {
        color = color.toLowerCase();
        return switch (color) {
            case "black" -> NamedTextColor.BLACK;
            case "dark_blue" -> NamedTextColor.DARK_BLUE;
            case "dark_green" -> NamedTextColor.DARK_GREEN;
            case "dark_aqua" -> NamedTextColor.DARK_AQUA;
            case "dark_red" -> NamedTextColor.DARK_RED;
            case "dark_purple" -> NamedTextColor.DARK_PURPLE;
            case "gold" -> NamedTextColor.GOLD;
            case "gray" -> NamedTextColor.GRAY;
            case "dark_gray" -> NamedTextColor.DARK_GRAY;
            case "blue" -> NamedTextColor.BLUE;
            case "green" -> NamedTextColor.GREEN;
            case "aqua" -> NamedTextColor.AQUA;
            case "red" -> NamedTextColor.RED;
            case "light_purple" -> NamedTextColor.LIGHT_PURPLE;
            case "yellow" -> NamedTextColor.YELLOW;
            case "white" -> NamedTextColor.WHITE;
            default -> {
                Constants.createErrorLog("Unknown Color value used: " + color);
                yield NamedTextColor.WHITE;
            }
        };
    }

    public static @NotNull String getChatMessageFormat(ServerPlayer player, User user) {
        CachedMetaData meta = user.getCachedData().getMetaData();

        String prefix = meta.getPrefix();
        String suffix = meta.getSuffix();

        if (!ReLPChatPrefix.config.mainConfig.enablePrefix) prefix = null;
        if (!ReLPChatPrefix.config.mainConfig.enableSuffix) suffix = null;

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        return getChatFormat(player, prefix, suffix, ReLPChatPrefix.config.mainConfig.messageFormat);
    }

    public static String replacePlayerPlaceholder(String playerName, String messageFormat) {
        if (messageFormat.contains("{player}")) {
            messageFormat = messageFormat.replace("{player}", playerName);
        }
        return messageFormat;
    }

    public static @NotNull String getChatFormat(ServerPlayer player, String prefix, String suffix, String messageFormat) {
        String playerName = player.getName().getString();

        if (messageFormat.contains("{prefix}")) {
            messageFormat = messageFormat.replace("{prefix}", prefix);
        }

        messageFormat = replacePlayerPlaceholder(playerName, messageFormat);

        if (messageFormat.contains("{suffix}")) {
            messageFormat = messageFormat.replace("{suffix}", suffix);
        }

        return messageFormat;
    }

    public static Component getMessageComponent(String playerMessage, @Nullable Component textAddition) {
        Component playerComponent = miniMessage.deserialize(playerMessage);
        if (textAddition == null) {
            return playerComponent;
        }
        return playerComponent.append(textAddition);
    }

    public static Component getMessageComponent(String playerMessage) {
        return getMessageComponent(playerMessage, null);
    }

    public static Component processUserMessage(String rawText, String messageColor) {
        NamedTextColor actualMessageColor = getAdventureColor(messageColor);
        return Component.text().content(" " + rawText)
                .color(actualMessageColor)
                .build();
    }
}
