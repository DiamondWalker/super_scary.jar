package diamondwalker.sscary.entity.entity.deadeye;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class DeadeyeShootingGoal extends Goal {
    private final EntityDeadeye owner;
    private int shotsLeft = 6;
    private int timeSinceShot;
    private final int firingInterval;
    private final double distSqr;

    protected DeadeyeShootingGoal(EntityDeadeye owner, int firingTime, double dist) {
        this.owner = owner;
        this.firingInterval = firingTime;
        this.distSqr = dist * dist;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return owner.getTarget() != null && owner.distanceToSqr(owner.getTarget()) < distSqr && owner.hasLineOfSight(owner.getTarget());
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && shotsLeft > 0;
    }

    @Override
    public void start() {
        owner.setShooting(true);
    }

    @Override
    public void stop() {
        owner.setShooting(false);
        shotsLeft = 6;
        timeSinceShot = 0;
    }

    @Override
    public void tick() {
        owner.getNavigation().stop();
        owner.getLookControl().setLookAt(owner.getTarget());
        timeSinceShot++;
        if (timeSinceShot > firingInterval) {
            // shoot
            timeSinceShot = 0;
            shotsLeft--;
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
