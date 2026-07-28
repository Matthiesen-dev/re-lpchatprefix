package dev.matthiesen.relpchatprefix.neoforge;

import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ServerChatEvent;

@Mod(ReLPChatPrefix.MOD_ID)
public final class ReLPChatPrefixNeoForge {
    public static final ReLPChatPrefix INSTANCE = ReLPChatPrefix.INSTANCE;

    public ReLPChatPrefixNeoForge() {
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        INSTANCE.initialize();
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChat(ServerChatEvent event) {
        try {
            INSTANCE.onServerChat(event.getPlayer(), event.getRawText());
            event.setCanceled(true);
        } catch (Exception e) {
            INSTANCE.createErrorLog("Error processing chat message: " + e, e);
        }
    }
}
