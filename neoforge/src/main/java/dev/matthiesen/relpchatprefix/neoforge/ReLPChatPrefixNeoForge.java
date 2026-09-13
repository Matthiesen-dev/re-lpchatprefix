package dev.matthiesen.relpchatprefix.neoforge;

import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(ReLPChatPrefix.MOD_ID)
public final class ReLPChatPrefixNeoForge {
    public static final ReLPChatPrefix INSTANCE = ReLPChatPrefix.INSTANCE;

    public ReLPChatPrefixNeoForge() {
        if (FMLEnvironment.dist.isClient()) {
            INSTANCE.createInfoLog("Skipping loading for NeoForge Mod Loader. (Client side note supported)");
        } else {
            INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
            INSTANCE.initialize();
        }
    }
}
