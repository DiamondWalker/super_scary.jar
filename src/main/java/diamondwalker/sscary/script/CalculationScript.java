package diamondwalker.sscary.script;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.data.CommonData;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.ColorOverlayData;
import diamondwalker.sscary.data.server.CalculationData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.handler.internal.PlayerFallHandler;
import diamondwalker.sscary.network.NarratorPacket;
import diamondwalker.sscary.randomevent.common.calculation.CalculationQuestion;
import diamondwalker.sscary.registry.SScaryDamageTypes;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.script.variable.*;
import diamondwalker.sscary.sound.CalculationSoundInstance;
import diamondwalker.sscary.util.ChatUtil;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

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

    private final StringVariable question = StringVariable.create().save("question").define(this);
    private final StringVariable answer = StringVariable.create().save("answer").define(this);
    private final IntegerVariable timeToAnswer = IntegerVariable.create().save("time").define(this);
    private final IntegerVariable ticks = IntegerVariable.create().save("ticks").define(this);

    public final EnumVariable<CalculationState> state = EnumVariable.create(CalculationState.class).sync().save("state").define(this);

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
        CalculationState oldState = state.get();

        if (!clientSide) {
            if (state.get() == CalculationState.NOT_ASKED) {
                if (ticks.get() >= 40) {
                    say(question.get());
                    setState(CalculationState.WAITING_FOR_ANSWER);
                }

            } else if (state.get() == CalculationState.WAITING_FOR_ANSWER || state.get() == CalculationState.CORRECT_BUT_WAITING || state.get() == CalculationState.INCORRECT_BUT_WAITING) {
                if (ticks.get() >= timeToAnswer.get()) {
                    if (state.get() == CalculationState.WAITING_FOR_ANSWER) {
                        say("Time's up!");
                    } else if (state.get() == CalculationState.CORRECT_BUT_WAITING) {
                        say(CORRECT_MESSAGES[random.nextInt(CORRECT_MESSAGES.length)]);
                    } else {
                        say(INCORRECT_MESSAGES[random.nextInt(INCORRECT_MESSAGES.length)]);
                    }

                    CalculationData data = WorldData.get(server).calculation;
                    if (state.get() == CalculationState.CORRECT_BUT_WAITING) {
                        data.score++;
                        if (data.score >= data.getScoreForGraduation()) {
                            setState(CalculationState.PREPARING_TO_GRADUATE);
                            data.grade++;
                            data.score = 0;
                        } else {
                            setState(CalculationState.LEAVE);
                        }
                    } else {
                        if (data.score > 0) data.score--;
                        setState(CalculationState.PREPARING_TO_PUNISH);
                    }
                }

            } else if (state.get() == CalculationState.PREPARING_TO_GRADUATE) {
                if (ticks.get() >= 60) setState(CalculationState.GRADUATION);

            } else if (state.get() == CalculationState.GRADUATION) {
                int grade = WorldData.get(server).calculation.grade;
                if (ticks.get() == 0) {
                    say("Congratulations, you just graduated from §e" + getGradeName(grade - 1) + "§r!");
                    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                        player.sendSystemMessage(Component.literal(player.getName().getString() + " is now in §e" + getGradeName(grade) + "§r")); // TODO: this should be per player
                    }
                } else if (random.nextInt(3) == 0) {
                    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                        double magnitude = 10 + random.nextDouble() * 20;
                        double angle = random.nextDouble() * Math.PI * 2;

                        BlockPos pos = BlockPos.containing(
                                Math.cos(angle) * magnitude + player.getX(),
                                0,
                                Math.sin(angle) * magnitude + player.getZ()
                        );
                        pos = pos.atY(player.level().getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()));
                        Vec3 spawnPos = pos.getBottomCenter();

                        Level level = player.level();
                        FireworkRocketEntity firework = new FireworkRocketEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, generateFirework());
                        level.addFreshEntity(firework);
                    }
                }
                if (ticks.get() >= 180 + grade * 20) {
                    setState(CalculationState.LEAVE);
                }

            } else if (state.get() == CalculationState.PREPARING_TO_PUNISH) {
                if (ticks.get() >= 60) setState(CalculationState.PUNISHMENT);

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

                        if (CommonData.ultraScaryMode) {
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
                throw new IllegalStateException("Illegal Calculation state: " + state);
            }
        }

        if (state.get() == oldState) ticks.set(ticks.get() + 1); // if state changed we will leave it at 0 for one tick
    }

    public void setState(CalculationState newState) {
        state.set(newState);
        ticks.set(0);
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

    private void say(String msg) {
        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CALCULATION_NAME, msg));

        msg = msg.replaceAll("-", "minus");
        msg = msg.replaceAll("x", "times");
        msg = msg.replaceAll("/", "divided by");

        msg = msg.replaceAll("§.", "");

        PacketDistributor.sendToAllPlayers(new NarratorPacket(msg));
    }

    private String getGradeName(int grade) {
        if (grade < 0) throw new IllegalStateException(grade + " is not a valid grade level!");

        if (grade == 0) return "Kindergarten";

        if (grade > 12) return "College";

        return grade + switch (grade) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        } + " Grade";
    }

    private ItemStack generateFirework() {
        IntList color = IntList.of(Util.getRandom(DyeColor.values(), random).getFireworkColor());
        IntList fadeColor = IntList.of();

        FireworkExplosion.Shape shape = Util.getRandom(FireworkExplosion.Shape.values(), random);
        int flightTime = random.nextInt(3) + 1;

        boolean trail = random.nextInt(10) == 0;
        boolean twinkle = random.nextInt(20) == 0;

        ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET);
        itemstack.set(
                DataComponents.FIREWORKS,
                new Fireworks(
                        (byte)flightTime,
                        List.of(new FireworkExplosion(shape, color, fadeColor, trail, twinkle))
                )
        );

        return itemstack;
    }

    public enum CalculationState {
        NOT_ASKED,
        WAITING_FOR_ANSWER,
        CORRECT_BUT_WAITING,
        INCORRECT_BUT_WAITING,
        PREPARING_TO_GRADUATE,
        GRADUATION,
        PREPARING_TO_PUNISH,
        PUNISHMENT,
        LEAVE
    }
}
