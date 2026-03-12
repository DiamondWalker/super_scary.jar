package diamondwalker.sscary.data.server;

import net.minecraft.nbt.CompoundTag;

public class BizarroData extends PersistentWorldData {
    public int bizarroEncounters = 0;
    public int cooldown = 0;

    BizarroData(WorldData data) {
        super(data);
    }

    @Override
    public String getId() {
        return "bizarro";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putInt("encounters", bizarroEncounters);
        tag.putInt("cooldown", cooldown);
    }

    @Override
    public void load(CompoundTag tag) {
        bizarroEncounters = tag.getInt("encounters");
        cooldown = tag.getInt("cooldown");
    }
}
