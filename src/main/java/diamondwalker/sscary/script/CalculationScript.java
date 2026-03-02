package diamondwalker.sscary.script;

import diamondwalker.sscary.randomevent.common.calculation.CalculationQuestion;
import diamondwalker.sscary.registry.SScaryDamageTypes;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CalculationScript extends Script {
    private String question;
    private String answer;
    private int timeToAnswer;
    private int ticks = 0;

    private CalculationState state = CalculationState.NOT_ASKED;

    public CalculationScript(MinecraftServer server) {
        super(SScaryScripts.CALCULATION.get(), server);
    }

    public CalculationScript(MinecraftServer server, CalculationQuestion question) {
        this(server);
        this.question = question.question;
        this.answer = question.answer;
        this.timeToAnswer = question.secondsToRespond * 20;
    }

    @Override
    public void onStart() {
        sendJoinMessage(ChatUtil.CALCULATION_NAME);
    }

    @Override
    public void tick() {
        if (state == CalculationState.NOT_ASKED) {
            if (ticks >= 40) {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, question));
                state = CalculationState.WAITING_FOR_ANSWER;
                ticks = 0;
            }

        } else if (state == CalculationState.WAITING_FOR_ANSWER || state == CalculationState.CORRECT_BUT_WAITING || state == CalculationState.INCORRECT_BUT_WAITING) {
            if (ticks >= timeToAnswer) {
                if (state == CalculationState.WAITING_FOR_ANSWER) {
                    chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, "Time's up!"));
                } else if (state == CalculationState.CORRECT_BUT_WAITING) {
                    chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, "Correct!"));
                } else {
                    chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, "Incorrect!"));
                }
                state = state != CalculationState.CORRECT_BUT_WAITING ? CalculationState.PUNISHMENT : CalculationState.LEAVE;
                ticks = 0;
            }

        } else if (state == CalculationState.PUNISHMENT) {
            if (ticks >= 20) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    player.hurt(SScaryDamageTypes.calculation(player), Float.MAX_VALUE);
                    for (int i = 0; i < 15; i++) {
                        Vec3 pos = player.position();
                        pos = pos.add(random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5);
                        player.level().explode(null, pos.x, pos.y, pos.z, 20.0f, Level.ExplosionInteraction.MOB);
                    }
                }
                state = CalculationState.LEAVE;
                ticks = 0;
            }

        } else if (state == CalculationState.LEAVE) {
            if (ticks >= 40) end();

        } else {
            throw new IllegalStateException("Illegal Calculation state: " + state.toString());
        }

        ticks++;
    }

    @Override
    public void onEnd() {
        sendLeaveMessage(ChatUtil.CALCULATION_NAME);
    }

    @Override
    public void handleChatInput(ServerPlayer sender, String message) {
        if (state == CalculationState.WAITING_FOR_ANSWER) { // make sure enough time has elapsed
            state = message.equals(answer) ? CalculationState.CORRECT_BUT_WAITING : CalculationState.INCORRECT_BUT_WAITING;
        }
    }

    @Override
    public void save(CompoundTag nbt) {
        nbt.putString("question", question);
        nbt.putString("answer", answer);
        nbt.putInt("time", timeToAnswer);
        nbt.putInt("result", state.ordinal());
        nbt.putInt("ticks", ticks);
    }

    @Override
    public void load(CompoundTag nbt) {
        question = nbt.getString("question");
        answer = nbt.getString("answer");
        timeToAnswer = nbt.getInt("time");
        state = CalculationState.values()[nbt.getInt("result")];
        ticks = nbt.getInt("ticks");
    }

    private enum CalculationState {
        NOT_ASKED,
        WAITING_FOR_ANSWER,
        CORRECT_BUT_WAITING,
        INCORRECT_BUT_WAITING,
        PUNISHMENT,
        LEAVE
    }
}
