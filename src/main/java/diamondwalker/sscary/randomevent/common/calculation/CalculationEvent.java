package diamondwalker.sscary.randomevent.common.calculation;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.data.server.CalculationData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.SScaryDamageTypes;
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

@EventBusSubscriber
public class CalculationEvent {
    public static boolean execute(MinecraftServer server) {
        WorldData data = WorldData.get(server);

        if (data.scripts.hasLock("calculation")) return false;

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
        AtomicBoolean blowUp = new AtomicBoolean(false);

        new ScriptBuilder(server, "calculation")
                .chatMessageForAll(ChatUtil.getJoinMessage(ChatUtil.CALCULATION_NAME))
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, question.question))
                .action((serv) -> {
                    CalculationData calc = WorldData.get(serv).calculation;
                    calc.expectedAnswer = question.answer;
                    calc.givenAnswer = null;
                })
                .rest(question.secondsToRespond * 20)
                .action((serv) -> {
                    CalculationData calc = WorldData.get(serv).calculation;
                    if (calc.givenAnswer == null) {
                        blowUp.set(true);
                        serv.getPlayerList().broadcastSystemMessage(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, "Time's up!"), false);
                    } else if (!calc.givenAnswer.equals(calc.expectedAnswer)) {
                        blowUp.set(true);
                        serv.getPlayerList().broadcastSystemMessage(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, "Incorrect!"), false);
                    } else {
                        blowUp.set(false);
                        serv.getPlayerList().broadcastSystemMessage(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, "Correct!"), false);
                    }

                    calc.givenAnswer = null;
                    calc.expectedAnswer = null;
                })
                .rest(20)
                .action((serv) -> {
                    if (blowUp.get()) {
                        for (ServerPlayer player : serv.getPlayerList().getPlayers()) {
                            player.hurt(SScaryDamageTypes.calculation(player), Float.MAX_VALUE);
                            for (int i = 0; i < 15; i++) {
                                Vec3 pos = player.position();
                                pos = pos.add(random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5);
                                player.level().explode(null, pos.x, pos.y, pos.z, 20.0f, Level.ExplosionInteraction.MOB);
                            }
                        }
                    }
                })
                .rest(40)
                .chatMessageForAll(ChatUtil.getLeaveMessage(ChatUtil.CALCULATION_NAME))
                .startScript();

        return true;
    }

    @SubscribeEvent
    private static void handlePlayerChat(ServerChatEvent event) {
        WorldData data = WorldData.get(event.getPlayer().server);
        if (data.calculation.expectedAnswer != null && data.calculation.givenAnswer == null && data.scripts.hasLock("calculation")) {
            String message = event.getMessage().getString();
            try {
                data.calculation.givenAnswer = String.valueOf(Long.valueOf(message));
            } catch (NumberFormatException ignored) {}
        }
    }
}
