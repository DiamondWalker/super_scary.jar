package diamondwalker.twais.data.server;

import net.minecraft.nbt.CompoundTag;

public class RandomEventData extends PersistentWorldData {
    public long timeSinceLastEvent = 0;
    public long timeForNextEvent = 0;

    RandomEventData(WorldData data) {
        super(data);
    }

    @Override
    public String getId() {
        return "random_events";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putLong("lastEventTime", timeSinceLastEvent);
        tag.putLong("nextEventTime", timeForNextEvent);
    }

    @Override
    public void load(CompoundTag tag) {
        timeSinceLastEvent = tag.getLong("lastEventTime");
        timeForNextEvent = tag.getLong("nextEventTime");
    }
}
