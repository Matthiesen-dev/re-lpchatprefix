package dev.matthiesen.common.relpchatprefix;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
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

public class ReLPChatPrefix {
    private static final ConfigManager<ModConfig> CONFIG_MANAGER =
            new ReLpChatPrefixConfigManager<>(ModConfig.class, "config");
    private static volatile LuckPerms luckPerms;
    private static MatthiesenLibTextParser textParser;

    public static void initialize() {
        CONFIG_MANAGER.loadConfig();
        MetricManager.init();
        AdventureCompat.init();

        // TODO: Make this configurable once this mod supports other parsers
        textParser = AdventureCompat.getParser();

        MatthiesenLibApi.registerPlayerEventHandler(Constants.MOD_ID, new PlayerEvents());
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
