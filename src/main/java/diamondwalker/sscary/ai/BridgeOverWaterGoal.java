package diamondwalker.sscary.ai;

import diamondwalker.sscary.entity.entity.deadeye.EntityDeadeye;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class BridgeOverWaterGoal extends Goal {
    private final EntityDeadeye mob;

    public BridgeOverWaterGoal(EntityDeadeye mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (!mob.isInFluidType() && mob.onGround()) {
            BlockState state = mob.level().getBlockState(mob.blockPosition().below());
            if (state.canBeReplaced() && !state.getFluidState().isEmpty()) {
                return true;
            }
        }

        if (mob.isInFluidType() && mob.isInFluidType((fluid, height) -> height < 0.5)) {
            BlockState state = mob.level().getBlockState(mob.blockPosition()); // not below this time because we're actually in the fluid
            if (state.canBeReplaced() && !state.getFluidState().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        if (mob.isInFluidType()) {
            double ogY = mob.getY();
            mob.setPos(mob.getX(), ogY + 0.5, mob.getZ());
            if (mob.isInWall()) {
                mob.setPos(mob.getX(), ogY, mob.getZ());
                return;
            }
        }

        // FIXME: pathfinding gets a little wonky while bridging
        mob.level().setBlock(mob.blockPosition().below(), Blocks.COBBLESTONE.defaultBlockState(), 2);
        mob.setBuilding(40);
    }
}
