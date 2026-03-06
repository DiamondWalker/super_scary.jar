package diamondwalker.sscary.script;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.ColorOverlayData;
import diamondwalker.sscary.handler.internal.PlayerFallHandler;
import diamondwalker.sscary.network.CalculationStatePacket;
import diamondwalker.sscary.randomevent.common.calculation.CalculationQuestion;
import diamondwalker.sscary.registry.SScaryDamageTypes;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.script.variable.*;
import diamondwalker.sscary.sound.CalculationSoundInstance;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class CalculationScript extends Script {
    private static final String[] CORRECT_MESSAGES = {
            "Correct!",
            "Good job!",
            "Great work!",
            "Splendid!",
            "Excellent!",
            "Very good!",
            "Well done!",
            "Great!",
            "Exemplary!",
            "Yes!",
            "Fantastic!",
            "Wonderful!",
            "Wow!",
            "Good one!",
            "You are smarter than Einstein!"
    };

    private static final String[] INCORRECT_MESSAGES = {
            "Incorrect!",
            "No!",
            "Try again!",
            "Fail!",
            "Failure!",
            "Wrong!",
            "'A' for effort!",
            "Better luck next time!",
            "Not quite!",
            "You should have studied harder!",
            "Your parents would be so disappointed in you!",
            "You get detention!",
            "You will amount to nothing in life!",
            "You have brought shame upon your family!",
            "You will be a janitor when you grow up!"
    };

    private final StringVariable question = variableManager.add(new StringVariable("").save("question"));
    private final StringVariable answer = variableManager.add(new StringVariable("").save("answer"));
    private final IntegerVariable timeToAnswer = variableManager.add(new IntegerVariable(0).save("time"));
    private final IntegerVariable ticks = variableManager.add(new IntegerVariable(0).save("ticks"));

    public final EnumVariable<CalculationState> state = variableManager.add(new EnumVariable<>(CalculationState.NOT_ASKED).save("state").sync());

    public CalculationScript(MinecraftServer server) {
        super(SScaryScripts.CALCULATION.get(), server);
    }

    public CalculationScript(int clientId) {
        super(SScaryScripts.CALCULATION.get(), clientId);
    }

    public CalculationScript(MinecraftServer server, CalculationQuestion question) {
        this(server);
        this.question.set(question.question);
        this.answer.set(question.answer);
        this.timeToAnswer.set(question.secondsToRespond * 20);
    }

    @Override
    public void onStart() {
        if (!clientSide) {
            sendJoinMessage(ChatUtil.CALCULATION_NAME);
        }
    }

    @Override
    public void tick() {
        if (!clientSide) {
            if (state.get() == CalculationState.NOT_ASKED) {
                if (ticks.get() >= 40) {
                    chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, question.get()));
                    setState(CalculationState.WAITING_FOR_ANSWER);
                }

            } else if (state.get() == CalculationState.WAITING_FOR_ANSWER || state.get() == CalculationState.CORRECT_BUT_WAITING || state.get() == CalculationState.INCORRECT_BUT_WAITING) {
                if (ticks.get() >= timeToAnswer.get()) {
                    if (state.get() == CalculationState.WAITING_FOR_ANSWER) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, "Time's up!"));
                    } else if (state.get() == CalculationState.CORRECT_BUT_WAITING) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, CORRECT_MESSAGES[random.nextInt(CORRECT_MESSAGES.length)]));
                    } else {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, INCORRECT_MESSAGES[random.nextInt(INCORRECT_MESSAGES.length)]));
                    }
                    setState(state.get() != CalculationState.CORRECT_BUT_WAITING ? CalculationState.PREPARING_TO_PUNISH : CalculationState.LEAVE);
                }

            } else if (state.get() == CalculationState.PREPARING_TO_PUNISH) {
                if (ticks.get() >= 40) setState(CalculationState.PUNISHMENT);
            } else if (state.get() == CalculationState.PUNISHMENT) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    double x = random.nextDouble() * 1.5 - 0.75;
                    double y = random.nextDouble() * 1 - 0.5;
                    double z = random.nextDouble() * 1.5 - 0.75;
                    Vec3 motion = new Vec3(x, y, z);
                    player.addDeltaMovement(motion);
                    player.hurtMarked = true;
                    PlayerFallHandler.disableFall(player);
                }

                if (ticks.get() >= 60) {
                    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                        player.hurt(SScaryDamageTypes.calculation(player), Float.MAX_VALUE);

                        if (Config.ULTRA_SCARY_MODE.get()) {
                            for (int i = 0; i < 15; i++) {
                                Vec3 pos = player.position();
                                pos = pos.add(random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5, random.nextDouble() * 10 - 5);
                                player.level().explode(null, pos.x, pos.y, pos.z, 20.0f, Level.ExplosionInteraction.MOB);
                            }
                        }
                    }
                    setState(CalculationState.LEAVE);
                }

            } else if (state.get() == CalculationState.LEAVE) {
                if (ticks.get() >= 40) end();

            } else {
                throw new IllegalStateException("Illegal Calculation state: " + state.toString());
            }
        }
        ticks.set(ticks.get() + 1);
    }

    public void setState(CalculationState newState) {
        state.set(newState);
        ticks.set(0);

        if (!clientSide) {
            PacketDistributor.sendToAllPlayers(new CalculationStatePacket(getSyncId(), state.get()));
        }
    }

    @Override
    public void onVariableUpdate(ScriptVariable<?, ?> updatedVariable) {
        super.onVariableUpdate(updatedVariable);

        if (updatedVariable == state && state.get() == CalculationState.PUNISHMENT) {
            CalculationScript ref = this;
            ClientData.get().colorOverlay = new ColorOverlayData(0.8f, 0.0f, 0.0f, 0.4f, 0) {
                @Override
                public boolean shouldContinue() {
                    return ref.state.get() == CalculationState.PUNISHMENT && !ref.hasEnded();
                }
            };
            Minecraft.getInstance().getSoundManager().queueTickingSound(new CalculationSoundInstance(this));
        }
    }

    @Override
    public void onEnd() {
        if (!clientSide) {
            sendLeaveMessage(ChatUtil.CALCULATION_NAME);
        }
    }

    @Override
    public void handleChatInput(ServerPlayer sender, String message) {
        if (state.get() == CalculationState.WAITING_FOR_ANSWER) { // make sure enough time has elapsed
            setState(message.equals(answer.get()) ? CalculationState.CORRECT_BUT_WAITING : CalculationState.INCORRECT_BUT_WAITING);
        }
    }

    /*@Override
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
    }*/

    public enum CalculationState {
        NOT_ASKED,
        WAITING_FOR_ANSWER,
        CORRECT_BUT_WAITING,
        INCORRECT_BUT_WAITING,
        PREPARING_TO_PUNISH,
        PUNISHMENT,
        LEAVE
    }
}
