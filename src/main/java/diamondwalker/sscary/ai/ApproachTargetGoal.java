package diamondwalker.sscary.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ApproachTargetGoal extends Goal {
    private int recalculationTime = 0;

    private final PathfinderMob mob;
    private final double speedModifier;

    private LivingEntity target;

    public ApproachTargetGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        target = mob.getTarget();
        return target != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (recalculationTime-- <= 0) return false;

        if (mob.getNavigation().isDone()) return false;

        if (!target.isAlive()) return false;

        return true;
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(target, speedModifier);
        recalculationTime = 100;
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        target = null;
    }
}
