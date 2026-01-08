package diamondwalker.twais.entity.corrupted;

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

public class EntityCorrupted extends Mob {
    private static final TargetingConditions LOOK_CONDITION = TargetingConditions.forNonCombat().ignoreLineOfSight();
    public EntityCorrupted(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        Player player = level().getNearestPlayer(LOOK_CONDITION, this);
        if (player != null && player.isAlive()) {
            this.getLookControl().setLookAt(player.getX(), player.getEyeY(), player.getZ());
        }
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
