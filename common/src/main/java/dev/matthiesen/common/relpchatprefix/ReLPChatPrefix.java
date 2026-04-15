package dev.matthiesen.common.relpchatprefix;

import dev.matthiesen.common.relpchatprefix.config.ConfigManager;
import dev.matthiesen.common.relpchatprefix.config.ModConfig;
import dev.matthiesen.common.relpchatprefix.formatting.Formatter;
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

    public static String getChatComponent(ServerPlayer player, String messageFormat) {
        LuckPerms luckPerms = getLuckPerms();

        if (luckPerms == null) {
            return null;
        }

        User user = luckPerms.getUserManager().getUser(player.getUUID());
        if (user == null) {
            Constants.LOGGER.debug("User data not found for: {}", player.getName().getString());
            return null;
        }

        return Formatter.getChatMessageFormat(player, user, messageFormat);
    }

    public static void onServerChat(ServerPlayer player, String rawText) {
        Audience serverChat = getAdventure().all();
        String messageFormat = getChatComponent(player, config.mainConfig.messageFormat);
        Component messageComponent = Formatter.processUserMessage(rawText, config.mainConfig.messageColor);
        Component finalComponent = Formatter.getMessageComponent(messageFormat, messageComponent);
        serverChat.sendMessage(finalComponent);
    }

    public static void onLogin(ServerPlayer player) {
        loginLogoutEvent(player, config.chatOverrides.joinMessage);
    }

    public static void onLogout(ServerPlayer player) {
        loginLogoutEvent(player, config.chatOverrides.leaveMessage);
    }

    public static void loginLogoutEvent(ServerPlayer player, String messageFormat) {
        Audience serverChat = getAdventure().all();
        messageFormat = getChatComponent(player, messageFormat);
        Component finalComponent = Formatter.getMessageComponent(messageFormat);
        serverChat.sendMessage(finalComponent);
    }
}
