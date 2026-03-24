package diamondwalker.sscary.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TargetOrDespawnGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public TargetOrDespawnGoal(Mob mob, Class<T> targetType, boolean mustSee) {
        super(mob, targetType, mustSee);
    }

    public TargetOrDespawnGoal(Mob mob, Class<T> targetType, boolean mustSee, Predicate<LivingEntity> targetPredicate) {
        super(mob, targetType, mustSee, targetPredicate);
    }

    public TargetOrDespawnGoal(Mob mob, Class<T> targetType, boolean mustSee, boolean mustReach) {
        super(mob, targetType, mustSee, mustReach);
    }

    public TargetOrDespawnGoal(Mob mob, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, targetType, randomInterval, mustSee, mustReach, targetPredicate);
    }

    @Override
    public boolean canUse() {
        this.findTarget();
        if (this.target != null) {
            return true;
        } else {
            ///this.mob.discard();
            return false;
        }
    }
}
