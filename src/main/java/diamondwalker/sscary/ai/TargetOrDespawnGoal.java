package diamondwalker.sscary.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TargetOrDespawnGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public TargetOrDespawnGoal(Mob mob, Class<T> targetType, boolean mustSeeToStart, boolean mustSeeToContinue) {
        super(mob, targetType, mustSeeToContinue);
        if (!mustSeeToStart) targetConditions.ignoreLineOfSight();
    }

    public TargetOrDespawnGoal(Mob mob, Class<T> targetType, boolean mustSeeToStart, boolean mustSeeToContinue, Predicate<LivingEntity> targetPredicate) {
        super(mob, targetType, mustSeeToContinue, targetPredicate);
        if (!mustSeeToStart) targetConditions.ignoreLineOfSight();
    }

    public TargetOrDespawnGoal(Mob mob, Class<T> targetType, boolean mustSeeToStart, boolean mustSeeToContinue, boolean mustReach) {
        super(mob, targetType, mustSeeToContinue, mustReach);
        if (!mustSeeToStart) targetConditions.ignoreLineOfSight();
    }

    public TargetOrDespawnGoal(Mob mob, Class<T> targetType, int randomInterval, boolean mustSeeToStart, boolean mustSeeToContinue, boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, targetType, randomInterval, mustSeeToContinue, mustReach, targetPredicate);
        if (!mustSeeToStart) targetConditions.ignoreLineOfSight();
    }

    @Override
    public boolean canUse() {
        this.findTarget();
        if (this.target != null) {
            return true;
        } else {
            this.mob.discard();
            return false;
        }
    }
}
