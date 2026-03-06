package diamondwalker.sscary.data.server;

import net.minecraft.nbt.CompoundTag;

public class CalculationData extends PersistentWorldData {
    public int impossibleQuestionCounter = 0;

    public int grade = 0;
    public int score = 0;

    CalculationData(WorldData data) {
        super(data);
    }

    public int getScoreForGraduation() {
        return 0;
        /*if (grade > 0) return Integer.MAX_VALUE;

        return 3 + grade;*/
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
