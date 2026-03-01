package diamondwalker.sscary.data.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.checkerframework.checker.units.qual.N;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class WorldData extends SavedData {
    public final ScriptData scripts = new ScriptData();
    public final FriendData friend = new FriendData(this);

    public final NewScriptsData newScripts = new NewScriptsData(this);
    public final CalculationData calculation = new CalculationData(this);

    public final CorruptedEntityBuildData corruptedEntityBuilds = new CorruptedEntityBuildData(this);

    public final ProgressionData progression = new ProgressionData(this);

    public final RandomEventData randomEvents = new RandomEventData(this);

    public final BizarroData bizarro = new BizarroData(this);

    public final VisageData visage = new VisageData(this);

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
