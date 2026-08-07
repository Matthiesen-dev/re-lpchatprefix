package dev.matthiesen.relpchatprefix.common;

import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import dev.matthiesen.relpchatprefix.common.config.ChatPrefixConfig;
import dev.matthiesen.relpchatprefix.common.events.PlayerEvents;
import dev.matthiesen.relpchatprefix.common.events.ServerEvents;
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

    public void initialize() {
        super.initialize();
        registerModConfig(MOD_ID, ModConfigType.SERVER, ChatPrefixConfig.SERVER_SPEC, "relpchatprefix/server.toml");

        PlatformEvents.SERVER_STARTED.subscribe(event -> loadTextParserFromConfig());
        PlatformEvents.SERVER_RELOAD.subscribe(event -> reload());
        PlatformEvents.SERVER_CHAT.subscribe(ServerEvents::onServerChat);
        PlatformEvents.PLAYER_JOIN.subscribe(PlayerEvents::onPlayerJoin);
        PlatformEvents.PLAYER_LEAVE.subscribe(PlayerEvents::onPlayerLeave);

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

    public void reload() {
        ChatPrefixConfig.SERVER_CONFIG.textParser.clearCache();
        loadTextParserFromConfig();
        createInfoLog("Configuration and Text Parser reloaded");
    }
}
