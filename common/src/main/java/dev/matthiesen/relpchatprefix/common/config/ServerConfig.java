package dev.matthiesen.relpchatprefix.common.config;

import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ServerConfig {

    // Main Configuration
    public ModConfigSpec.EnumValue<BuiltInTextParsers> textParser;
    public ModConfigSpec.BooleanValue enablePrefix;
    public ModConfigSpec.BooleanValue enableSuffix;
    public ModConfigSpec.ConfigValue<String> messageFormat;
    public ModConfigSpec.EnumValue<ChatFormatting> messageColor;

    // Chat Overrides
    public ModConfigSpec.ConfigValue<String> chatOverrides_joinMessage;
    public ModConfigSpec.ConfigValue<String> chatOverrides_leaveMessage;

    // First Join
    public ModConfigSpec.BooleanValue firstJoin_enable;
    public ModConfigSpec.ConfigValue<String> firstJoin_message;

    public ServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("Re-LPChatPrefix Configuration").push("main");

        textParser = builder.comment("The text parser to use for formatting messages.")
                .defineEnum("textParser", BuiltInTextParsers.ADVENTURE);
        enablePrefix = builder.comment("Enable or disable the use of prefixes in chat messages.")
                .define("enablePrefix", true);
        enableSuffix = builder.comment("Enable or disable the use of suffixes in chat messages.")
                .define("enableSuffix", true);
        messageFormat = builder.comment("The format of chat messages. Use {prefix}, {player}, and {suffix} as placeholders.")
                .define("messageFormat", "{prefix}{player}{suffix} <gray><bold>»<reset>");
        messageColor = builder.comment("The color of the chat message text.")
                .defineEnum("messageColor", ChatFormatting.WHITE);

        builder.comment("Chat Overrides Configuration").push("chatOverrides");
        chatOverrides_joinMessage = builder.comment("The message format for player join messages.")
                .define("joinMessage", "<yellow>{prefix}{player}{suffix} joined the game<reset>");
        chatOverrides_leaveMessage = builder.comment("The message format for player leave messages.")
                .define("leaveMessage", "<yellow>{prefix}{player}{suffix} left the game<reset>");
        builder.pop();

        builder.comment("First Join Configuration").push("firstJoin");
        firstJoin_enable = builder.comment("Enable or disable the first join welcome message.")
                .define("enable", false);
        firstJoin_message = builder.comment("The welcome message format for players joining for the first time.")
                .define("message", "<yellow>Welcome {player} to the server!<reset>");
        builder.pop();

        builder.pop();
    }
}
