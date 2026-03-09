package diamondwalker.sscary.script;

import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.entity.entity.bizarrodude.EntityBizarroDude;
import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.registry.SScaryEntities;
import diamondwalker.sscary.registry.SScaryMusic;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.script.variable.IntegerVariable;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class FriedSteveScript extends Script {
    public static final String[] MESSAGES = new String[] {
            "I can see you.",
            "I can feel your heart beating.",
            "Do you feel safe?",
            "Can you see me?",
            "I'm here.",
            "It'll all be over soon.",
            "You skin is so soft. I can't wait to see it rip.",
            "I'll make this quick.",
            "I'm coming for you.",
    };

    private final IntegerVariable timeElapsed = IntegerVariable.create().save("ticks").sync().define(this);
    private final IntegerVariable timeToSummon = IntegerVariable.create().save("spawnTime").sync().define(this);

    private final IntegerVariable attemptCooldown = IntegerVariable.create().define(this);

    public FriedSteveScript(MinecraftServer server) {
        super(SScaryScripts.FRIED_STEVE.get(), server);
    }

    public FriedSteveScript(int clientId) {
        super(SScaryScripts.FRIED_STEVE.get(), clientId);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!clientSide) {
            timeToSummon.set(90 * 20 + random.nextInt(60 * 20 + 1)); // 1.5 to 2.5 minutes
            server.getPlayerList().broadcastSystemMessage(ChatUtil.getEntityChatMessage(EntityFriedSteve.getName(random), MESSAGES[random.nextInt(MESSAGES.length)]), false);
        }
    }

    @Override
    public void tick() {
        timeElapsed.set(timeElapsed.get() + 1);
        if (!clientSide) {
            WorldData.get(server).randomEvents.timeSinceLastEvent = 0;

            if (timeElapsed.get() >= timeToSummon.get()) {
                if (attemptCooldown.get() <= 0) {
                    ServerPlayer[] players = RandomEvent.getValidPlayers(server);
                    if (players.length > 0) {
                        ServerPlayer player = players[random.nextInt(players.length)];

                        Vec3 dir = player.getLookAngle();
                        dir = new Vec3(dir.x, 0, dir.z).normalize();
                        if (dir.lengthSqr() > 0) { // in case you're looking straight up. idk if it's possible but just to be safe
                            Vec3 spawn = player.position().add(dir.scale(2.3));
                            BlockPos spawnPos = BlockPos.containing(spawn);
                            Level level = player.level();
                            if (level.getBlockState(spawnPos.below()).isSolid()) {
                                EntityFriedSteve entity = SScaryEntities.FRIED_STEVE.get().create(level);//.spawn((ServerLevel)player.level(), spawnPos, MobSpawnType.MOB_SUMMONED);
                                entity.moveTo(spawn);

                                if (level.noCollision(entity) && player.hasLineOfSight(entity)) {
                                    Vec3 relative = player.getEyePosition().subtract(entity.getEyePosition());
                                    double horizontalComponent = Math.sqrt(relative.x * relative.x + relative.z * relative.z);
                                    double yRot = Math.toDegrees(Math.atan2(relative.z, relative.x)) - 90;
                                    double xRot = -Math.toDegrees(Math.atan2(relative.y, horizontalComponent));
                                    entity.yHeadRot = entity.yBodyRot = (float)yRot;
                                    entity.setXRot((float)xRot);
                                    level.addFreshEntity(entity);
                                    end();
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    attemptCooldown.set(attemptCooldown.get() - 1);
                }
            }
        }
    }

    @Override
    public void modifyLightmap(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {
        Vector3f redColor = new Vector3f(colors).mul(0.6f, 0.04f, 0.04f);
        float f = (partialTicks + timeElapsed.get()) / /*(20 * 40)*/timeToSummon.get(); // fade lasts 40 seconds
        f = (float)Math.pow(f, 1.0 / 3);
        f = Mth.clamp(f, 0.0f, 1.0f);
        colors.lerp(redColor, f);
    }

    @Override
    public Music getMusic() {
        if (timeElapsed.get() > timeToSummon.get() / 3) return SScaryMusic.FRIED_STEVE_PRELUDE;
        return null;
    }
}
