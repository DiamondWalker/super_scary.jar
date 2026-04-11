package diamondwalker.sscary.randomevent.common.calculation;

import diamondwalker.sscary.data.CommonData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.script.randomevent.CalculationScript;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public class CalculationEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        WorldData data = WorldData.get(server);

        RandomSource random = server.overworld().getRandom();
        data.calculation.impossibleQuestionCounter++;
        boolean impossible = data.calculation.impossibleQuestionCounter >= 5 && CommonData.ultraScaryMode;

        CalculationQuestion question;
        if (impossible) {
            question = QuestionProvider.generateImpossibleQuestion(random);
            data.calculation.impossibleQuestionCounter = 0;
        } else {
            question = QuestionProvider.generateRegularQuestion(data.calculation.grade, random);
        }

        return data.newScripts.startScript(new CalculationScript(server, question));
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.COMMON;
    }
}
