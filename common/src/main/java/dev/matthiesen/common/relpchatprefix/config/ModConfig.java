package dev.matthiesen.common.relpchatprefix.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class ModConfig {
    @SerializedName("mainConfig")
    public MainConfig mainConfig = new MainConfig();

    public static class MainConfig {
        @SerializedName("enablePrefix")
        public boolean enablePrefix = true;

        @SerializedName("enableSuffix")
        public boolean enableSuffix = true;

        @SerializedName("messageFormat")
        public String messageFormat = "{prefix}{player}{suffix}: ";

        @SerializedName("messageColor")
        public String messageColor = "white";
    }


    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
