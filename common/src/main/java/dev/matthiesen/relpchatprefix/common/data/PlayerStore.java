package dev.matthiesen.relpchatprefix.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class PlayerStore extends SavedData {
    private final Map<String, Integer> playerStore = new HashMap<>();

    public PlayerStore() {}

    public static PlayerStore create() {
        return new PlayerStore();
    }

    public static PlayerStore load(CompoundTag nbt, HolderLookup.Provider provider) {
        PlayerStore data = create();
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

    public static final SavedData.Factory<PlayerStore> FACTORY = new SavedData.Factory<>(
            PlayerStore::create, // Constructor if data doesn't exist
            PlayerStore::load, // Method to load data
            null
    );
}
