package diamondwalker.sscary.data.server;

import net.minecraft.nbt.CompoundTag;

public class RandomEventData extends PersistentWorldData {
    public int timeSinceLastEvent = 0;
    public int timeForNextEvent = 0;

    public int prevMax;
    public int prevMin;
    public int prevMed;

    RandomEventData(WorldData data) {
        super(data);
    }

    @Override
    public String getId() {
        return "random_events";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putInt("lastEventTime", timeSinceLastEvent);
        tag.putInt("nextEventTime", timeForNextEvent);

        tag.putInt("prevMax", prevMax);
        tag.putInt("prevMin", prevMin);
        tag.putInt("prevMed", prevMed);
    }

    @Override
    public void load(CompoundTag tag) {
        timeSinceLastEvent = tag.getInt("lastEventTime");
        timeForNextEvent = tag.getInt("nextEventTime");

        prevMax = tag.getInt("prevMax");
        prevMin = tag.getInt("prevMin");
        prevMed = tag.getInt("prevMed");
    }
}
