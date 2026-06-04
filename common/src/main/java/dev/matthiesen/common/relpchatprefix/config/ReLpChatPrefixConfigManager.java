package dev.matthiesen.common.relpchatprefix.config;

import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;
import dev.matthiesen.common.relpchatprefix.Constants;

public class ReLpChatPrefixConfigManager<T> extends ConfigManager<T> {
    public ReLpChatPrefixConfigManager(Class<T> configClass, String configName) {
        super(configClass, configName, Constants.MOD_ID);
    }
}
