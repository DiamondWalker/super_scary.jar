package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.ai.ImprovedMeleeAttackGoal;
import net.minecraft.world.entity.PathfinderMob;

public class FriedSteveChaseGoal extends ImprovedMeleeAttackGoal {
    private final EntityFriedSteve steve;

    public FriedSteveChaseGoal(EntityFriedSteve mob, double speedModifier) {
        super(mob, speedModifier, true);
        steve = mob;
    }

    @Override
    public boolean canUse() {
        return steve.getState() == EnumFriedSteveState.CHASING && super.canUse();
    }
}
