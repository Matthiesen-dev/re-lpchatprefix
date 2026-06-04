package dev.matthiesen.common.relpchatprefix.util;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ServerUtil {
    public static MinecraftServer getServer() {
        return MatthiesenLibApi.getMinecraftServer();
    }

    public static void sendToAllAndConsole(Component message) {
        MinecraftServer server = getServer();
        if (server == null) return;

        // Send to all players
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(message, false);
        }

        // Send to the console
        server.sendSystemMessage(message);
    }
}
