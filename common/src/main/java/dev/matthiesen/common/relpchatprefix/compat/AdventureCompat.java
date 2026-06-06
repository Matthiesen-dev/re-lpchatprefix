package dev.matthiesen.common.relpchatprefix.compat;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibTextParser;
import dev.matthiesen.common.relpchatprefix.Constants;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;

public class AdventureCompat {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static void init() {
        MatthiesenLibApi.registerTextParser(createAdventureTextParser());
    }

    private static MatthiesenLibTextParser createAdventureTextParser() {
        return new MatthiesenLibTextParser() {
            @Override
            public String getType() {
                return "adventure";
            }

            @Override
            public Component parse(String text) {
                try {
                    var adventureComponent = miniMessage.deserialize(text);
                    var json = JSONComponentSerializer.json().serialize(adventureComponent);
                    return Component.Serializer.fromJson(json, RegistryAccess.EMPTY);
                } catch (RuntimeException e) {
                    Constants.createErrorLog("Failed to parse text with Adventure parser: " + text, e);
                    return Component.literal(text);
                }
            }
        };
    }

    public static MatthiesenLibTextParser getParser() {
        return MatthiesenLibApi.getTextParser("adventure");
    }
}
