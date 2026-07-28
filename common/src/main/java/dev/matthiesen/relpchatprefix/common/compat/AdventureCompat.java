package dev.matthiesen.relpchatprefix.common.compat;

import dev.matthiesen.matthiesen_core.common.api.text_parsers.TextParser;
import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;

public final class AdventureCompat {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static void init() {
        ReLPChatPrefix.INSTANCE.getTextParserManager().registerTextParser(createAdventureTextParser());
    }

    private static TextParser createAdventureTextParser() {
        return new TextParser() {
            @Override
            public String type() {
                return "adventure";
            }

            @Override
            public Component parse(String text) {
                try {
                    var adventureComponent = miniMessage.deserialize(text);
                    var json = JSONComponentSerializer.json().serialize(adventureComponent);
                    return Component.Serializer.fromJson(json, RegistryAccess.EMPTY);
                } catch (RuntimeException e) {
                    ReLPChatPrefix.INSTANCE.createErrorLog("Failed to parse text with Adventure parser: " + text, e);
                    return Component.literal(text);
                }
            }
        };
    }

    public static TextParser getParser() {
        return ReLPChatPrefix.INSTANCE.getTextParserManager().getTextParser("adventure");
    }
}
