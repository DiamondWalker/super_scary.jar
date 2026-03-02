package diamondwalker.sscary.randomevent.common.calculation;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.data.server.CalculationData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.SScaryDamageTypes;
import diamondwalker.sscary.script.CalculationScript;
import diamondwalker.sscary.util.ChatUtil;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class CalculationEvent {
    public static boolean execute(MinecraftServer server) {
        WorldData data = WorldData.get(server);

        RandomSource random = server.overworld().getRandom();
        data.calculation.impossibleQuestionCounter++;
        boolean impossible = data.calculation.impossibleQuestionCounter >= 5 && Config.ULTRA_SCARY_MODE.get();

        CalculationQuestion question;
        if (impossible) {
            question = QuestionProvider.generateImpossibleQuestion(random);
            data.calculation.impossibleQuestionCounter = 0;
        } else {
            question = QuestionProvider.generateRegularQuestion(12, random);
        }

        return data.newScripts.startScript(new CalculationScript(server, question));
    }
}
