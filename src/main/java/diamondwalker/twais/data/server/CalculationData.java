package diamondwalker.twais.data.server;

import net.minecraft.nbt.CompoundTag;

public class CalculationData extends PersistentWorldData {
    public int questionCount = 0;
    public String expectedAnswer = null;
    public String givenAnswer = null;

    CalculationData(WorldData data) {
        super(data);
    }

    @Override
    public String getId() {
        return "calculation";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putInt("questions", questionCount);
    }

    @Override
    public void load(CompoundTag tag) {
        questionCount = tag.getInt("questions");
    }
}
