package dev.matthiesen.common.relpchatprefix;

import dev.matthiesen.common.relpchatprefix.config.ConfigManager;
import dev.matthiesen.common.relpchatprefix.config.ModConfig;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ReLPChatPrefix {
    public static ModConfig config;
    public static LuckPerms luckPerms;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static volatile MinecraftServerAudiences adventure;

    public static MinecraftServerAudiences getAdventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure without a running server!");
        }
        return adventure;
    }

    public static void initialize() {
        Constants.createInfoLog("Initialized");
        config = new ConfigManager().loadConfig();
    }

    public static void onStartup(MinecraftServer server) {
        Constants.createInfoLog("Server starting, Setting up");
        adventure = MinecraftServerAudiences.of(server);
    }

    public static void onShutdown() {
        Constants.createInfoLog("Server stopping, shutting down");
        new ConfigManager().saveConfig();
        adventure = null;
    }

    public static LuckPerms getLuckPerms() {
        if (luckPerms == null) {
            try {
                luckPerms = LuckPermsProvider.get();
                Constants.createInfoLog("LuckPerms API loaded successfully");
            } catch (IllegalStateException e) {
                Constants.createErrorLog("LuckPerms not available, chat prefix will not be applied");
                return null;
            }
        }
        return luckPerms;
    }

    public static void onServerChat(ServerPlayer player, String rawText) {
        LuckPerms luckPerms = getLuckPerms();
        Audience serverChat = getAdventure().all();

        if (luckPerms == null) {
            return;
        }

        User user = luckPerms.getUserManager().getUser(player.getUUID());
        if (user == null) {
            Constants.LOGGER.debug("User data not found for: {}", player.getName().getString());
            return;
        }

        String messageFormat = getChatMessageFormat(player, user);
        NamedTextColor messageColor = Constants.getAdventureColor(config.mainConfig.messageColor);
        Component playerComponent = miniMessage.deserialize(messageFormat);
        Component messageComponent = Component.text()
                .content(" " + rawText)
                .color(messageColor)
                .build();

        Component finalComponent = playerComponent.append(messageComponent);
        serverChat.sendMessage(finalComponent);
    }

    public static void loginLogoutEvent(ServerPlayer player, String messageFormat) {
        Audience serverChat = getAdventure().all();
        String playerName = player.getName().getString();

        if (messageFormat.contains("{player}")) {
            messageFormat = messageFormat.replace("{player}", playerName);
        }

        Component finalComponent = miniMessage.deserialize(messageFormat);
        serverChat.sendMessage(finalComponent);
    }

    public static void onLogin(ServerPlayer player) {
        loginLogoutEvent(player, config.chatOverrides.joinMessage);
    }

    public static void onLogout(ServerPlayer player) {
        loginLogoutEvent(player, config.chatOverrides.leaveMessage);
    }

    private static @NotNull String getChatMessageFormat(ServerPlayer player, User user) {
        CachedMetaData meta = user.getCachedData().getMetaData();

        String prefix = meta.getPrefix();
        String suffix = meta.getSuffix();

        if (!config.mainConfig.enablePrefix) prefix = null;
        if (!config.mainConfig.enableSuffix) suffix = null;

        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";

        return getChatFormat(player, prefix, suffix);
    }

    private static @NotNull String getChatFormat(ServerPlayer player, String prefix, String suffix) {
        String playerName = player.getName().getString();
        String messageFormat = config.mainConfig.messageFormat;

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
}
