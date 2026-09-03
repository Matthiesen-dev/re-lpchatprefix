package dev.matthiesen.relpchatprefix.common;

import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import dev.matthiesen.matthiesen_core.common.api.events.server.ServerEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import dev.matthiesen.matthiesen_core.common.utility.chat.ServerMessagingUtil;
import dev.matthiesen.relpchatprefix.common.config.ChatPrefixConfig;
import dev.matthiesen.relpchatprefix.common.data.PlayerStore;
import dev.matthiesen.relpchatprefix.common.util.Formatter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public final class ReLPChatPrefix extends AbstractCommonMod {
    public static final String MOD_ID = "relpchatprefix";
    public static final String MOD_NAME = "Re-LPChatPrefix";
    public static @Token final String METRICS_TOKEN = "b34b7080ee595daa3d1ecd8dbfe6ada7";

    public static final ReLPChatPrefix INSTANCE = new ReLPChatPrefix();

    private static TextParser textParser;

    public ReLPChatPrefix() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public @NotNull @Token String getMetricsToken() {
        return METRICS_TOKEN;
    }

    private boolean isServerRunning = false;

    public void initialize() {
        super.initialize();
        registerModConfig(MOD_ID, ModConfigType.SERVER, ChatPrefixConfig.SERVER_SPEC);

        PlatformEvents.SERVER_STARTED.subscribe(this::onServerStarted);
        PlatformEvents.SERVER_RELOAD.subscribe(this::onServerReload);
        PlatformEvents.SERVER_CHAT.subscribe(this::onServerChat);
        PlatformEvents.PLAYER_JOIN.subscribe(this::onPlayerJoin);
        PlatformEvents.PLAYER_LEAVE.subscribe(this::onPlayerLeave);
        PlatformEvents.SERVER_STOPPING.subscribe(this::onServerStopping);

        createInfoLog("Initialized");
    }

    public TextParser getTextParser() {
        if (textParser == null) {
            throw new IllegalStateException("Text parser not initialized!");
        }
        return textParser;
    }

    public void loadTextParserFromConfig() {
        var parserName = ChatPrefixConfig.SERVER_CONFIG.textParser.get();
        switch (parserName) {
            case BuiltInTextParsers.ADVENTURE -> textParser = getTextParserManager().getTextParser(BuiltInTextParsers.ADVENTURE);
            case BuiltInTextParsers.VANILLA -> textParser = getTextParserManager().getTextParser(BuiltInTextParsers.VANILLA);
            case BuiltInTextParsers.EMBERS -> textParser = getTextParserManager().getTextParser(BuiltInTextParsers.EMBERS);
            default -> throw new IllegalStateException("Unexpected value: " + parserName);
        }
    }

    public void onServerStarted(ServerEvent.Started event) {
        createInfoLog("Loading Re-LPChatPrefix configuration and text parser");
        isServerRunning = true;
        loadTextParserFromConfig();
    }

    public void onServerStopping(ServerEvent.Stopping event) {
        isServerRunning = false;
    }

    public void onServerReload(ServerEvent.Reload event) {
        if (!isServerRunning) return;
        ChatPrefixConfig.SERVER_CONFIG.textParser.clearCache();
        loadTextParserFromConfig();
        createInfoLog("Configuration and Text Parser reloaded");
    }

    public boolean onServerChat(ServerEvent.Chat event) {
        if (!isServerRunning) return false;
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
    public void onPlayerJoin(PlayerEvent.Join event) {
        if (!isServerRunning) return;
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

    public void onPlayerLeave(PlayerEvent.Leave event) {
        if (!isServerRunning) return;
        ServerPlayer player = event.player();
        loginLogoutEvent(player, ChatPrefixConfig.SERVER_CONFIG.chatOverrides_leaveMessage.get());
    }

    private void loginLogoutEvent(ServerPlayer player, String messageFormat) {
        try {
            messageFormat = Formatter.getChatComponent(player, messageFormat);
            Component message = Formatter.getMessageComponent(messageFormat);
            ServerMessagingUtil.sendToAllAndConsole(message);
        } catch (RuntimeException e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Error handling player login/logout event for " + player.getName().getString(), e);
        }
    }
}
