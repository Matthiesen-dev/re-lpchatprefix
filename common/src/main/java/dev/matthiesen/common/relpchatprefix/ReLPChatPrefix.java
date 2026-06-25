package dev.matthiesen.common.relpchatprefix;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibTextParser;
import dev.matthiesen.common.relpchatprefix.compat.AdventureCompat;
import dev.matthiesen.common.relpchatprefix.config.ModConfig;
import dev.matthiesen.common.relpchatprefix.config.ReLpChatPrefixConfigManager;
import dev.matthiesen.common.relpchatprefix.events.PlayerEvents;
import dev.matthiesen.common.relpchatprefix.events.ServerEvents;
import dev.matthiesen.common.relpchatprefix.util.MetricManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class ReLPChatPrefix {
    private static final ConfigManager<ModConfig> CONFIG_MANAGER =
            new ReLpChatPrefixConfigManager<>(ModConfig.class, "config");
    private static volatile LuckPerms luckPerms;
    private static MatthiesenLibTextParser textParser;

    private static final List<String> availableTextParsers = List.of(
            "adventure",
            "vanilla",
            "emberstextapi"
    );

    public static void initialize() {
        CONFIG_MANAGER.loadConfig();
        MetricManager.init();
        AdventureCompat.init();

        loadTextParserFromConfig();

        MatthiesenLibApi.registerPlayerEventHandler(Constants.MOD_ID, PlayerEvents.getInstance());
        MatthiesenLibApi.registerReloadRunnable(Constants.MOD_ID, ReLPChatPrefix::reload);
        Constants.createInfoLog("Initialized");
    }

    public static void onServerChat(ServerPlayer player, String rawText) {
        ServerEvents.onServerChat(player, rawText);
    }

    public static MatthiesenLibTextParser getTextParser() {
        if (textParser == null) {
            throw new IllegalStateException("Text parser not initialized!");
        }
        return textParser;
    }

    public static void loadTextParserFromConfig() {
        var config = CONFIG_MANAGER.getConfig();
        var parserName = config.textParser;
        if (!availableTextParsers.contains(parserName)) {
            Constants.createErrorLog("Invalid text parser specified in config: " + parserName);
            Constants.createErrorLog("Available text parsers: " + String.join(", ", availableTextParsers));
            Constants.createInfoLog("Defaulting to 'adventure' text parser.");
            parserName = "adventure";
        }

        switch (parserName) {
            case "adventure" -> textParser = AdventureCompat.getParser();
            case "vanilla" -> textParser = MatthiesenLibApi.getTextParser(MatthiesenLibBuiltInTextParsers.VANILLA);
            case "emberstextapi" -> textParser = MatthiesenLibApi.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
            default -> throw new IllegalStateException("Unexpected value: " + parserName);
        }
    }

    public static void reload() {
        CONFIG_MANAGER.loadConfig();
        loadTextParserFromConfig();
        Constants.createInfoLog("Configuration and Text Parser reloaded");
    }

    public static ModConfig getConfig() {
        return CONFIG_MANAGER.getConfig();
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
}
