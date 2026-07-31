package dev.matthiesen.relpchatprefix.common;

import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import dev.matthiesen.matthiesen_core.common.utility.config.ConfigManager;
import dev.matthiesen.relpchatprefix.common.config.ModConfig;
import dev.matthiesen.relpchatprefix.common.events.PlayerEvents;
import dev.matthiesen.relpchatprefix.common.events.ServerEvents;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ReLPChatPrefix extends AbstractCommonMod {
    public static final String MOD_ID = "relpchatprefix";
    public static final String MOD_NAME = "Re-LPChatPrefix";
    public static @Token final String METRICS_TOKEN = "b34b7080ee595daa3d1ecd8dbfe6ada7";

    public static final ReLPChatPrefix INSTANCE = new ReLPChatPrefix();

    private static final ConfigManager<ModConfig> CONFIG_MANAGER =
            INSTANCE.createConfigManager(ModConfig.class, "config");
    private static TextParser textParser;

    private static final List<String> availableTextParsers = List.of(
            "adventure",
            "vanilla",
            "emberstextapi"
    );

    public ReLPChatPrefix() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public @NotNull @Token String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public void initialize() {
        super.initialize();
        CONFIG_MANAGER.loadConfig();

        loadTextParserFromConfig();

        PlatformEvents.SERVER_RELOAD.subscribe(event -> reload());

        PlatformEvents.PLAYER_JOIN.subscribe(PlayerEvents::onPlayerJoin);
        PlatformEvents.PLAYER_LEAVE.subscribe(PlayerEvents::onPlayerLeave);

        createInfoLog("Initialized");
    }

    public void onServerChat(ServerPlayer player, String rawText) {
        ServerEvents.onServerChat(player, rawText);
    }

    public TextParser getTextParser() {
        if (textParser == null) {
            throw new IllegalStateException("Text parser not initialized!");
        }
        return textParser;
    }

    public void loadTextParserFromConfig() {
        var config = CONFIG_MANAGER.getConfig();
        var parserName = config.textParser;
        if (!availableTextParsers.contains(parserName)) {
            createErrorLog("Invalid text parser specified in config: " + parserName);
            createErrorLog("Available text parsers: " + String.join(", ", availableTextParsers));
            createInfoLog("Defaulting to 'adventure' text parser.");
            parserName = "adventure";
        }

        switch (parserName) {
            case "adventure" -> textParser = getTextParserManager().getTextParser(BuiltInTextParsers.ADVENTURE);
            case "vanilla" -> textParser = getTextParserManager().getTextParser(BuiltInTextParsers.VANILLA);
            case "emberstextapi" -> textParser = getTextParserManager().getTextParser(BuiltInTextParsers.EMBERS);
            default -> throw new IllegalStateException("Unexpected value: " + parserName);
        }
    }

    public void reload() {
        CONFIG_MANAGER.loadConfig();
        loadTextParserFromConfig();
        createInfoLog("Configuration and Text Parser reloaded");
    }

    public ModConfig getConfig() {
        return CONFIG_MANAGER.getConfig();
    }
}
