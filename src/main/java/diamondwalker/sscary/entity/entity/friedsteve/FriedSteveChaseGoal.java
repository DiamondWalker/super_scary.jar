package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.ai.ImprovedMeleeAttackGoal;
import net.minecraft.world.entity.PathfinderMob;

public class FriedSteveChaseGoal extends ImprovedMeleeAttackGoal {
    private final EntityFriedSteve steve;

    public FriedSteveChaseGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier, true);
        steve = (EntityFriedSteve) mob;
    }

    @Override
    public boolean canUse() {
        return steve.isChasing() && super.canUse();
    }
}
