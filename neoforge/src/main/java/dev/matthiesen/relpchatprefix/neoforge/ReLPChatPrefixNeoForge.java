package dev.matthiesen.relpchatprefix.neoforge;

import dev.matthiesen.relpchatprefix.common.ReLPChatPrefix;
import net.neoforged.fml.common.Mod;

@Mod(ReLPChatPrefix.MOD_ID)
public final class ReLPChatPrefixNeoForge {
    public static final ReLPChatPrefix INSTANCE = ReLPChatPrefix.INSTANCE;

    public ReLPChatPrefixNeoForge() {
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        INSTANCE.initialize();
    }
}
