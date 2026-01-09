package diamondwalker.twais.entity.corrupted;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.WorldUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class EntityCorrupted extends Mob {
    private static final TargetingConditions LOOK_CONDITION = TargetingConditions.forNonCombat().ignoreLineOfSight();

    public EntityCorrupted(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        Player player = level().getNearestPlayer(LOOK_CONDITION, this);
        if (!level().isClientSide() && player != null && player.isAlive()) {
            this.getLookControl().setLookAt(player.getX(), player.getEyeY(), player.getZ());

            if (distanceTo(player) < 25 && hasLineOfSight(player)) {
                double angle = Mth.atan2(player.getZ() - getZ(), player.getX() - getX()) - 90;
                fillText(WorldUtil.placeSign(level(), blockPosition(), angle));
                this.discard();
            }
        }
    }

    private void fillText(WorldUtil.SignWriter writer) {
        boolean bugUnlocked = WorldData.get(getServer()).progression.hasSeenBug();
        switch (random.nextInt(bugUnlocked ? 7 : 6)) {
            case 0: {
                writer
                        .setFrontLine(1, "ur mom");

                break;
            }
            case 1: {
                writer
                        .setFrontLine(1, "go away fatty");

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
                        .setFrontLine(1, "you are cooked")
                        .setFrontLine(2, "broski");
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
                        .setFrontLine(0, "You are more")
                        .setFrontLine(1, "annoying than")
                        .setFrontLine(2, "a fucking")
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
                .add(Attributes.FOLLOW_RANGE, 20);
    }
}
