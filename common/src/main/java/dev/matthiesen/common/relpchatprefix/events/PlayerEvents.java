package dev.matthiesen.common.relpchatprefix.events;

import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibPlayerEventHandler;
import dev.matthiesen.common.relpchatprefix.Constants;
import dev.matthiesen.common.relpchatprefix.ReLPChatPrefix;
import dev.matthiesen.common.relpchatprefix.config.ModConfig;
import dev.matthiesen.common.relpchatprefix.data.PlayerStore;
import dev.matthiesen.common.relpchatprefix.util.Formatter;
import dev.matthiesen.common.relpchatprefix.util.ServerUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class PlayerEvents implements MatthiesenLibPlayerEventHandler {
    @Override
    public void onPlayerJoin(ServerPlayer player) {
        ModConfig config = ReLPChatPrefix.getConfig();
        loginLogoutEvent(player, config.chatOverrides.joinMessage);

        MinecraftServer server = ServerUtil.getServer();
        ServerLevel level = server.overworld();
        PlayerStore store = level.getDataStorage().computeIfAbsent(PlayerStore.FACTORY, Constants.MOD_ID);
        String playerUUID = player.getStringUUID();

        if (!store.hasBeenSeen(playerUUID)) {
            store.setSeen(playerUUID);
            if (!config.firstJoin.enable) return;
            String loginFormat = Formatter.getChatComponent(player, config.firstJoin.message);
            Component message = Formatter.getMessageComponent(loginFormat);
            ServerUtil.sendToAllAndConsole(message);

        }
    }

    @Override
    public void onPlayerLeave(ServerPlayer player) {
        loginLogoutEvent(player, ReLPChatPrefix.getConfig().chatOverrides.leaveMessage);
    }

    public static void loginLogoutEvent(ServerPlayer player, String messageFormat) {
        messageFormat = Formatter.getChatComponent(player, messageFormat);
        Component message = Formatter.getMessageComponent(messageFormat);
        ServerUtil.sendToAllAndConsole(message);
    }
}
