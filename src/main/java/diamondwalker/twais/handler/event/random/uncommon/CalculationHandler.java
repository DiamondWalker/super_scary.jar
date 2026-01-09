package diamondwalker.twais.handler.event.random.uncommon;

import diamondwalker.twais.Config;
import diamondwalker.twais.data.server.CalculationData;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.registry.TWAISDamageTypes;
import diamondwalker.twais.util.ChatUtil;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber
public class CalculationHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && !data.scripts.hasLock("calculation") && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(Config.UNCOMMON_EVENT_CHANCE.getAsInt()) == 0) {
                data.calculation.questionCount++;
                boolean impossible = data.calculation.questionCount >= 5;

                CalculationQuestion question;
                if (impossible) {
                    question = CalculationQuestion.generateImpossibleQuestion(random);
                    data.calculation.questionCount = 0;
                } else {
                    question = CalculationQuestion.generateRegularQuestion(random);
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
                        .rest(impossible ? 200 : 60)
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
                                    for (int i = 0; i < 15; i++) {
                                        Vec3 pos = player.position();
                                        pos = pos.add(random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5);
                                        player.level().explode(null, pos.x, pos.y, pos.z, 20.0f, Level.ExplosionInteraction.MOB);
                                    }
                                    player.hurt(TWAISDamageTypes.calculation(player), Float.MAX_VALUE);
                                }
                            }
                        })
                        .rest(40)
                        .chatMessageForAll(ChatUtil.getLeaveMessage(ChatUtil.CALCULATION_NAME))
                        .startScript();

                data.eventCooldown();
            }
        }
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

    private static class CalculationQuestion {
        private String question;
        private String answer;

        private static final String[] NAMES = new String[] {
                "Voorhees",
                "Myers",
                "Krueger",
                "Lecter"
        };

        private CalculationQuestion(String question) {
            this(question, "");
        }

        private CalculationQuestion(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        private static CalculationQuestion generateRegularQuestion(RandomSource random) {
            int opeerand1 = random.nextInt(10) + 1;
            int opeerand2 = random.nextInt(10) + 1;
            String answerString = opeerand1 + " + " + opeerand2 + " = ";

            return new CalculationQuestion(answerString, String.valueOf(opeerand1 + opeerand2));
        }

        private static CalculationQuestion generateImpossibleQuestion(RandomSource random) {
            String question;
            switch (random.nextInt(4)) {
                case 0: {
                    String name = NAMES[random.nextInt(NAMES.length)];
                    int principal = (random.nextInt(99) + 1) * 1000;
                    int rate = random.nextInt(5) + 1;
                    question = "Mr. " + name + " has opened a savings account with a principal of " + principal + ". " +
                            "This account has an interest rate of " + rate + "%, compounded continuously. " +
                            "How much money will Mr. " + name + " have saved by the time the heat death of the universe occurs in a googol years? " +
                            "(Helpful tip: 1 googol = 10^100)";
                    break;
                }
                case 1: {
                    question = "What is the numerical value of the imaginary unit i?";
                    break;
                }
                case 2: {
                    int digits = 256 + random.nextInt(501);
                    question = "Please type the first " + digits + " digits of PI.";
                    break;
                }
                default: {
                    int[] exponents = new int[4];
                    for (int i = 0; i < exponents.length; i++) {
                        int newNum;
                        SEARCH: while (true) {
                            newNum = random.nextInt(9) + 1;
                            for (int j = 0; j < i; j++) {
                                if (exponents[j] == newNum) continue SEARCH;
                            }
                            break;
                        }

                        exponents[i] = newNum;
                    }

                    Arrays.sort(exponents);

                    int bound1;
                    int bound2;
                    do {
                        bound1 = random.nextInt(-10, 10);
                        bound2 = random.nextInt(-10, 10);
                    } while (bound1 == bound2);

                    question = "Consider the graph of function f(x) = " + (random.nextInt(9) + 1) + "^" + exponents[3] + " + " +
                            (random.nextInt(9) + 1) + "^" + exponents[2] + " + " +
                            (random.nextInt(9) + 1) + "^" + exponents[1] + " + " +
                            (random.nextInt(9) + 1) + "^" + exponents[0] + ". " +
                            "Use integration to find the area under the graph of f when bounded by x = " + Math.min(bound1, bound2) +
                            " and x = " + Math.max(bound1, bound2) + ".";

                    break;
                }
            }

            return new CalculationQuestion(question);
        }
    }
}
