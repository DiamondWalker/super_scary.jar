package diamondwalker.sscary.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class TargetPlayerAnywhereGoal extends TargetGoal {
    public TargetPlayerAnywhereGoal(Mob mob) {
        super(mob, false);
    }

    @Override
    public boolean canUse() {
        targetMob = mob.level().getNearestPlayer(mob, getFollowDistance());
        return targetMob != null;
    }

    @Override
    protected double getFollowDistance() {
        return Float.MAX_VALUE;
    }
}
