package diamondwalker.sscary.data.server;

import net.minecraft.nbt.CompoundTag;

public class VisageData extends PersistentWorldData {
    public int spawnTicks = 0;
    public boolean visageEncountered = false;

    VisageData(WorldData data) {
        super(data);
    }

    @Override
    public String getId() {
        return "visage";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putInt("spawn", spawnTicks);
        tag.putBoolean("encounter", visageEncountered);
    }

    @Override
    public void load(CompoundTag tag) {
        spawnTicks = tag.getInt("spawn");
        visageEncountered = tag.getBoolean("encounter");
    }
}
