package diamondwalker.sscary.data.server;

import net.minecraft.nbt.CompoundTag;

public class CalculationData extends PersistentWorldData {
    public String expectedAnswer = null;
    public String givenAnswer = null;

    public int impossibleQuestionCounter = 0;

    CalculationData(WorldData data) {
        super(data);
    }

    @Override
    public String getId() {
        return "calculation";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putInt("impossibleQuestionCounter", impossibleQuestionCounter);
    }

    @Override
    public void load(CompoundTag tag) {
        impossibleQuestionCounter = tag.getInt("impossibleQuestionCounter");
    }
}
