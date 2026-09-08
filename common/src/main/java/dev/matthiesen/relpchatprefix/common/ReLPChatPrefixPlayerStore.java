package dev.matthiesen.relpchatprefix.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ReLPChatPrefixPlayerStore extends SavedData {
    private final Map<String, Integer> playerStore = new HashMap<>();

    public ReLPChatPrefixPlayerStore() {}

    public static ReLPChatPrefixPlayerStore create() {
        return new ReLPChatPrefixPlayerStore();
    }

    public static ReLPChatPrefixPlayerStore load(CompoundTag nbt, HolderLookup.Provider provider) {
        ReLPChatPrefixPlayerStore data = create();
        CompoundTag entriesNBT = nbt.getCompound("entries");
        for (String key : entriesNBT.getAllKeys()) {
            data.playerStore.put(key, entriesNBT.getInt(key));
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
        CompoundTag entriesNBT = new CompoundTag();
        playerStore.forEach(entriesNBT::putInt);
        nbt.put("entries", entriesNBT);
        return nbt;
    }

    public void setSeen(String key) {
        playerStore.put(key, 1);
        this.setDirty();
    }

    public boolean hasBeenSeen(String key) {
        int exists = playerStore.getOrDefault(key, 0);
        return exists != 0;
    }

    public static final SavedData.Factory<ReLPChatPrefixPlayerStore> FACTORY = new SavedData.Factory<>(
            ReLPChatPrefixPlayerStore::create, // Constructor if data doesn't exist
            ReLPChatPrefixPlayerStore::load, // Method to load data
            null
    );

    private static ReLPChatPrefixPlayerStore getPlayerStore() {
        MinecraftServer server = ReLPChatPrefix.INSTANCE.getCommonUtils().getServer();
        ServerLevel level = server.overworld();
        return level.getDataStorage().computeIfAbsent(ReLPChatPrefixPlayerStore.FACTORY, ReLPChatPrefix.MOD_ID);
    }

    private static volatile ReLPChatPrefixPlayerStore instance;

    private static ReLPChatPrefixPlayerStore getInstance() {
        if (instance == null) {
            synchronized (ReLPChatPrefixPlayerStore.class) {
                if (instance == null) {
                    instance = getPlayerStore();
                }
            }
        }
        return instance;
    }

    public static boolean hasPlayerBeenSeen(String key) {
        return getInstance().hasBeenSeen(key);
    }

    public static void markPlayerAsSeen(String key) {
        getInstance().setSeen(key);
    }
}
