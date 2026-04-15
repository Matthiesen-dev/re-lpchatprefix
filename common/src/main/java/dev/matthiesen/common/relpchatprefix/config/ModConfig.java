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
        public String messageFormat = "{prefix}{player}{suffix} <gray><bold>»<reset>";

        @SerializedName("messageColor")
        public String messageColor = "white";
    }

    @SerializedName("chatOverrides")
    public ChatOverrides chatOverrides = new ChatOverrides();

    public static class ChatOverrides {
        @SerializedName("joinMessage")
        public String joinMessage = "<yellow>{prefix}{player}{suffix} joined the game<reset>";

        @SerializedName("leaveMessage")
        public String leaveMessage = "<yellow>{prefix}{player}{suffix} left the game<reset>";
    }

    @SerializedName("firstJoin")
    public FirstJoin firstJoin = new FirstJoin();

    public static class FirstJoin {
        @SerializedName("message")
        public String message = "<yellow>Welcome to the server {player}!";

        @SerializedName("enable")
        public boolean enable = false;
    }

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
