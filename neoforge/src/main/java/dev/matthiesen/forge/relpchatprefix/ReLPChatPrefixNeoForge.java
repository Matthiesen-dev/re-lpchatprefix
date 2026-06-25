package dev.matthiesen.forge.relpchatprefix;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.relpchatprefix.ReLPChatPrefix;
import dev.matthiesen.common.relpchatprefix.Constants;
import dev.matthiesen.forge.relpchatprefix.text_parser.MatthiesenLibEmbersTextParserNeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ServerChatEvent;

@Mod(Constants.MOD_ID)
public class ReLPChatPrefixNeoForge {
    public ReLPChatPrefixNeoForge() {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        ReLPChatPrefix.initialize();
        NeoForge.EVENT_BUS.register(this);

        // If Ember's Text API is loaded, but MatthiesenLib (The Client package) is not installed, let's make sure Ember's is available
        if (
                MatthiesenLibApi.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName()) &&
                        !MatthiesenLibApi.isModLoaded("matthiesen_lib")
        ) {
            MatthiesenLibApi.registerTextParser(new MatthiesenLibEmbersTextParserNeoForge());
        }
    }

    @SubscribeEvent
    public void onChat(ServerChatEvent event) {
        try {
            ReLPChatPrefix.onServerChat(event.getPlayer(), event.getRawText());
            event.setCanceled(true);
        } catch (Exception e) {
            Constants.createErrorLog("Error processing chat message: " + e, e);
        }
    }
}
