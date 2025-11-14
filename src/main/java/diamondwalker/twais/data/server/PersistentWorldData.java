package diamondwalker.twais.data.server;

import net.minecraft.nbt.CompoundTag;

public abstract class PersistentWorldData {
    protected final WorldData mainData;

    PersistentWorldData(WorldData data) {
        this.mainData = data;
    }

    public abstract String getId();

    public abstract void save(CompoundTag tag);

    public abstract void load(CompoundTag tag);
}
