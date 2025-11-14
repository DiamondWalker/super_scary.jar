package diamondwalker.twais.data.server;

import diamondwalker.twais.Config;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class WorldData extends SavedData {
    public final ScriptData scripts = new ScriptData();
    public final FriendData friend = new FriendData(this);

    public final CalculationData calculation = new CalculationData(this);

    public final CorruptedEntityBuildData corruptedEntityBuilds = new CorruptedEntityBuildData(this);

    public final ProgressionData progression = new ProgressionData(this);

    private int eventCooldown = 0;

    private final ArrayList<PersistentWorldData> persistentData = new ArrayList<>();

    public WorldData() {
        try {
            for (Field field : WorldData.class.getDeclaredFields()) {
                if (field.get(this) instanceof PersistentWorldData persistentWorldData) {
                    persistentData.add(persistentWorldData);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void eventCooldown() {
        eventCooldown = Config.MIN_EVENT_INTERVAL.get();
    }

    public boolean areEventsOnCooldown() {
        return eventCooldown > 0;
    }

    public void decrementEventCooldown() {
        if (areEventsOnCooldown()) eventCooldown--;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        for (PersistentWorldData data : persistentData) {
            CompoundTag subTag = new CompoundTag();
            data.save(subTag);
            tag.put(data.getId(), subTag);
        }

        return tag;
    }

    public static WorldData load(CompoundTag nbt) {
        WorldData data = new WorldData();

        for (PersistentWorldData persistentWorldData : data.persistentData) {
            persistentWorldData.load(nbt.getCompound(persistentWorldData.getId()));
        }

        return data;
    }

    public static WorldData get(MinecraftServer server) {
        WorldData data = server.overworld().getDataStorage().computeIfAbsent(new SavedData.Factory<>(WorldData::new, (nbt, provider) -> WorldData.load(nbt)), "twais_data");
        data.setDirty();
        return data;
    }
}
