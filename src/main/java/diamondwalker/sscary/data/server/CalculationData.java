package diamondwalker.sscary.data.server;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

public class CalculationData extends PersistentWorldData {
    public int impossibleQuestionCounter = 0;

    public int grade = 0;
    public int score = 0;

    CalculationData(WorldData data) {
        super(data);
    }

    public int getScoreForGraduation() {
        if (grade > 12) return Integer.MAX_VALUE;

        return 3 + grade;
    }

    @Override
    public String getId() {
        return "calculation";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putInt("grade", grade);
        tag.putInt("score", score);
        tag.putInt("impossibleQuestionCounter", impossibleQuestionCounter);
    }

    @Override
    public void load(CompoundTag tag) {
        grade = tag.getInt("grade");
        score = tag.getInt("score");
        impossibleQuestionCounter = tag.getInt("impossibleQuestionCounter");
    }
}
