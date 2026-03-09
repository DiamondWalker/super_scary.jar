package diamondwalker.sscary.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class LookAtFarawayPlayerGoal extends Goal {
    private static final TargetingConditions LOOK_CONDITION = TargetingConditions.forNonCombat().ignoreLineOfSight();

    private Player lookingAt = null;
    private final Mob owner;

    public LookAtFarawayPlayerGoal(Mob owner) {
        this.owner = owner;
    }

    @Override
    public boolean canUse() {
        Player player = owner.level().getNearestPlayer(LOOK_CONDITION, owner);
        if (player != null && player.isAlive()) {
            lookingAt = player;
            return true;

        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return lookingAt.isAlive();
    }

    @Override
    public void tick() {
        double offset = lookingAt.getEyeY() - owner.getEyeY();
        owner.getLookControl().setLookAt(lookingAt.getX(), owner.getEyeY() + offset, lookingAt.getZ());
    }

    @Override
    public void stop() {
        lookingAt = null;
    }

    public Player getLookingAt() {
        return lookingAt;
    }
}
