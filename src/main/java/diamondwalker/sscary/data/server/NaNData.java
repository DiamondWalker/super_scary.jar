package diamondwalker.sscary.data.server;

import net.minecraft.nbt.CompoundTag;

public class NaNData extends PersistentWorldData {
    public boolean offended = false;

    NaNData(WorldData data) {
        super(data);
    }

    @Override
    public String getId() {
        return "NaN";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putBoolean("offended", offended);
    }

    @Override
    public void load(CompoundTag tag) {
        offended = tag.getBoolean("offended");
    }
}
