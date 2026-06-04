package dev.matthiesen.forge.relpchatprefix;

import dev.matthiesen.common.relpchatprefix.ReLPChatPrefix;
import dev.matthiesen.common.relpchatprefix.Constants;
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
    }

    @SubscribeEvent
    public void onChat(ServerChatEvent event) {
        try {
            ReLPChatPrefix.onServerChat(event.getPlayer(), event.getRawText());
            event.setCanceled(true);
        } catch (Exception e) {
            Constants.createErrorLog("Error processing chat message: " + e);
        }
    }
}
