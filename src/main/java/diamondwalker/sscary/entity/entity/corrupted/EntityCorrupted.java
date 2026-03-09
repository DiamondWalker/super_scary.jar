package diamondwalker.sscary.entity.entity.corrupted;

import diamondwalker.sscary.ai.LookAtFarawayPlayerGoal;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.SScarySounds;
import diamondwalker.sscary.util.ChatUtil;
import diamondwalker.sscary.util.ScriptBuilder;
import diamondwalker.sscary.util.WorldUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityCorrupted extends Mob {
    private LookAtFarawayPlayerGoal lookAtPlayer;

    public EntityCorrupted(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    int delay = 0;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, lookAtPlayer = new LookAtFarawayPlayerGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide()) {
            if (tickCount > 20 * 60 * 4) this.discard();

            if (lookAtPlayer.getLookingAt() != null) {
                Player player = lookAtPlayer.getLookingAt();
                if (delay > 0) {
                    delay--;
                    if (delay == 0) {
                        setPos(player.position());
                        this.setPos(player.position()); // teleport again to ensure you don't miss
                        if (this.isWithinMeleeAttackRange(player) && this.getSensing().hasLineOfSight(player)) {
                            if (this.doHurtTarget(player)) {
                                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
                                MinecraftServer server = getServer();
                                if (server != null && !WorldData.get(server).scripts.hasLock("corrupted_entity")) {
                                    new ScriptBuilder(getServer(), "corrupted_entity")
                                            .rest(60)
                                            .action((serv) -> {
                                                if (!player.isAlive()) {
                                                    serv.getPlayerList().broadcastSystemMessage(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "what a loser lmao"), false);
                                                }
                                            }).startScript();
                                }
                            }
                        }

                        discard();
                    }

                    return;
                }

                this.getLookControl().setLookAt(player.getX(), player.getEyeY(), player.getZ());

                if (distanceTo(player) < 25 && getSensing().hasLineOfSight(player)) {
                    if (!player.getAbilities().invulnerable && random.nextInt(3) == 0) {
                        delay = 7;
                        Vec3 target = player.position();
                        this.teleportTo(target.x, target.y, target.z);
                        this.playSound(SScarySounds.CORRUPTED_JUMPSCARE.value());
                        return;
                    }

                    double angle = Mth.atan2(player.getZ() - getZ(), player.getX() - getX()) - Math.PI / 2;
                    fillText(WorldUtil.placeSign(level(), blockPosition(), angle));
                    this.discard();
                }
            }
        }
    }

    private void fillText(WorldUtil.SignWriter writer) {
        boolean bugUnlocked = WorldData.get(getServer()).progression.hasSeenBug();
        switch (random.nextInt(bugUnlocked ? 10 : 9)) {
            case 0: {
                writer.setFrontLine(1, "ur mom");
                break;
            }
            case 1: {
                writer.setFrontLine(1, "go away fatty");
                break;
            }
            case 2: {
                writer.setFrontLine(1, "sup");
                break;
            }
            case 3: {
                writer.setFrontLine(1, "gtfo");
                break;
            }
            case 4: {
                writer
                        .setFrontLine(0, "you are so")
                        .setFrontLine(1, "cooked lmao");
                break;
            }
            case 5: {
                writer
                        .setFrontLine(1, "Shouldnt of")
                        .setFrontLine(2, "broken my box");
                break;
            }
            case 6: {
                writer
                        .setFrontLine(0, "Stop stalking me")
                        .setFrontLine(1, "you weirdo");
                break;
            }
            case 7: {
                writer
                        .setFrontLine(1, "I know where")
                        .setFrontLine(2, "you live");
                break;
            }
            case 8: {
                writer
                        .setFrontLine(1, "i sharted");
                break;
            }
            case 9: {
                writer
                        .setFrontLine(0, "You are more")
                        .setFrontLine(1, "annoying than a")
                        .setFrontLine(2, "fucking")
                        .setFrontLine(3, "inventory bug");
                break;
            }
        }

        writer.write();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                //.add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 5);
    }
}
