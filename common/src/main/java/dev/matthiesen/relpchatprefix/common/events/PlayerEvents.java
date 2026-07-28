package dev.matthiesen.relpchatprefix.common.events;

import dev.matthiesen.matthiesen_core.common.utility.chat.ServerMessagingUtil;
import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import dev.matthiesen.relpchatprefix.common.config.ModConfig;
import dev.matthiesen.relpchatprefix.common.data.PlayerStore;
import dev.matthiesen.relpchatprefix.common.util.Formatter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerEvents {
    public static final PlayerEvents INSTANCE = new PlayerEvents();

    public void onPlayerJoin(ServerPlayer player) {
        ModConfig config = ReLPChatPrefix.INSTANCE.getConfig();
        loginLogoutEvent(player, config.chatOverrides.joinMessage);

        try {
            MinecraftServer server = ReLPChatPrefix.INSTANCE.getCommonUtils().getServer();
            ServerLevel level = server.overworld();
            PlayerStore store = level.getDataStorage().computeIfAbsent(PlayerStore.FACTORY, ReLPChatPrefix.MOD_ID);
            String playerUUID = player.getStringUUID();

            if (!store.hasBeenSeen(playerUUID)) {
                store.setSeen(playerUUID);
                if (!config.firstJoin.enable) return;
                String loginFormat = Formatter.getChatComponent(player, config.firstJoin.message);
                Component message = Formatter.getMessageComponent(loginFormat);
                ServerMessagingUtil.sendToAllAndConsole(server, message);

            }
        } catch (RuntimeException e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Error handling player join event for " + player.getName().getString(), e);
        }
    }

    public void onPlayerLeave(ServerPlayer player) {
        loginLogoutEvent(player, ReLPChatPrefix.INSTANCE.getConfig().chatOverrides.leaveMessage);
    }

    public void loginLogoutEvent(ServerPlayer player, String messageFormat) {
        try {
            messageFormat = Formatter.getChatComponent(player, messageFormat);
            Component message = Formatter.getMessageComponent(messageFormat);
            ServerMessagingUtil.sendToAllAndConsole(message);
        } catch (RuntimeException e) {
            ReLPChatPrefix.INSTANCE.createErrorLog("Error handling player login/logout event for " + player.getName().getString(), e);
        }
    }
}
