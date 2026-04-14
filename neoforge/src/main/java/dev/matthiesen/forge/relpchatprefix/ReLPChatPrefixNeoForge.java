package dev.matthiesen.forge.relpchatprefix;

import dev.matthiesen.common.relpchatprefix.ReLPChatPrefix;
import dev.matthiesen.common.relpchatprefix.Constants;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@Mod(Constants.MOD_ID)
public class ReLPChatPrefixNeoForge {

    public ReLPChatPrefixNeoForge(IEventBus modBus) {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        ReLPChatPrefix.initialize();
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        ReLPChatPrefix.onStartup(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerStopping(ServerStoppingEvent event) {
        ReLPChatPrefix.onShutdown();
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
