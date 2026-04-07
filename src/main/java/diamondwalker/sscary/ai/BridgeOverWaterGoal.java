package diamondwalker.sscary.ai;

import diamondwalker.sscary.entity.entity.deadeye.EntityDeadeye;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;

import java.util.EnumSet;

public class BridgeOverWaterGoal extends Goal {
    private final EntityDeadeye mob;
    private BlockPos buildPos;
    private boolean jumpOut = false;

    public BridgeOverWaterGoal(EntityDeadeye mob) {
        this.mob = mob;

        if (!GoalUtils.hasGroundPathNavigation(mob)) {
            throw new IllegalArgumentException("Unsupported mob type for BridgeOverWaterGoal");
        }
    }

    @Override
    public boolean canUse() {
        if (!GoalUtils.hasGroundPathNavigation(mob)) return false;

        if (!mob.isInFluidType() && mob.onGround()) {
            buildPos = mob.blockPosition().below();

            BlockState state = mob.level().getBlockState(buildPos);
            if (state.canBeReplaced() && !state.getFluidState().isEmpty()) {
                jumpOut = false;
                return true;
            }
        }

        if (mob.isInFluidType() && mob.isInFluidType((fluid, height) -> height < 0.5)) {
            buildPos = mob.blockPosition();
            BlockState state = mob.level().getBlockState(mob.blockPosition()); // not below this time because we're actually in the fluid
            if (state.canBeReplaced() && !state.getFluidState().isEmpty()) {
                jumpOut = true;
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
        // jumpOut is true when we're inside the water and want to exit it
        if (jumpOut) {
            double ogY = mob.getY();
            mob.setPos(mob.getX(), ogY + 0.5, mob.getZ());
            if (mob.isInWall()) {
                mob.setPos(mob.getX(), ogY, mob.getZ());
                return;
            }
        }

        mob.level().setBlock(buildPos, Blocks.COBBLESTONE.defaultBlockState(), 2);
        mob.setBuilding(40);

        // if we don't do this, the next node will be buried inside the block, causing the mob to hesitate as it tries to go inside the cobblestone
        Path path = mob.getNavigation().getPath();
        if (path != null) {
            if (path.getNextNodePos().equals(buildPos)) path.advance();
        }
    }
}
