package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FriedSteveTeleportGoal extends Goal {
    private final EntityFriedSteve steve;
    private final int triggerTime;
    private final int teleportTime;
    private final int testAccuracy;

    private int noPathTicks = 0;
    private int teleportTicks = 0;

    public FriedSteveTeleportGoal(EntityFriedSteve mob, int timeBeforeTriggering, int timeBeforeTeleport, int testAccuracy) {
        steve = mob;
        triggerTime = timeBeforeTriggering;
        teleportTime = timeBeforeTeleport;
        this.testAccuracy = testAccuracy;
    }

    @Override
    public boolean canUse() {
        if (steve.getState() == EnumFriedSteveState.CHASING && steve.getTarget() != null) {
            // if he's already teleporting, we don't start it
            // this is to prevent conflicts if we're running multiple of the tasks (which we are, for accuracy levels)
            if (!steve.isTeleporting()) {
                if (!EntityUtil.hasPathTo(steve, EntityUtil.findSolidGroundUnder(steve.getTarget()), testAccuracy)) {
                    if (noPathTicks++ >= triggerTime) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        noPathTicks = 0;
        return false;
    }

    @Override
    public void start() {
        teleportTicks = teleportTime;
        steve.setTeleportingTo(EntityUtil.findSolidGroundUnder(steve.getTarget()));
    }

    @Override
    public boolean canContinueToUse() {
        return teleportTicks-- > 0;
    }

    @Override
    public void stop() {
        steve.setPos(steve.getTeleportingTo().getBottomCenter());
        steve.setTeleportingTo(null);
        if (steve.isInWall()) {
            steve.level().explode(steve, steve.getX(), steve.getY(0.5), steve.getZ(), 5, Level.ExplosionInteraction.MOB);
        }
        steve.particles();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
