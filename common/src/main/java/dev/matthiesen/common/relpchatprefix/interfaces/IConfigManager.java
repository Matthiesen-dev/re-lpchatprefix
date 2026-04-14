package dev.matthiesen.common.relpchatprefix.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.matthiesen.common.relpchatprefix.config.ModConfig;

public interface IConfigManager {
    ModConfig loadConfig();
    JsonElement mergeConfigs(JsonObject defaultConfig, JsonObject fileConfig);
    void saveConfig();
}
