package diamondwalker.sscary.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public class OpenTrapdoorGoal extends Goal {
    private final Mob mob;
    private int cooldown = 0;
    private BlockPos trapdoor = BlockPos.ZERO;

    public OpenTrapdoorGoal(Mob mob) {
        this.mob = mob;
        if (!GoalUtils.hasGroundPathNavigation(mob)) {
            throw new IllegalArgumentException("Unsupported mob type for OpenTrapdoorGoal");
        }
    }

    @Override
    public boolean canUse() {
        if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
            return false;
        } else if (!this.mob.horizontalCollision && !this.mob.verticalCollision) {
            return false;
        } else {
            GroundPathNavigation groundpathnavigation = (GroundPathNavigation)this.mob.getNavigation();
            Path path = groundpathnavigation.getPath();
            if (path != null && !path.isDone()) {
                for (int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); i++) {
                    Node node = path.getNode(i);

                    trapdoor = new BlockPos(node.x, node.y, node.z);
                    if (isTrapdoor(trapdoor)) return true;

                    trapdoor = trapdoor.above();
                    if (isTrapdoor(trapdoor)) return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (cooldown > 0) {
            cooldown--;
            return true;
        }

        return false;
    }

    private boolean isTrapdoor(BlockPos pos) {
        if (!(this.mob.distanceToSqr((double)pos.getX(), this.mob.getY(), (double)pos.getZ()) > 2.25)) {
            BlockState state = mob.level().getBlockState(pos);
            if (state.getBlock() instanceof TrapDoorBlock trapdoor && trapdoor.type.canOpenByHand()) {
                if (!state.getValue(TrapDoorBlock.OPEN)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void start() {
        cooldown = 5;
        BlockState state = mob.level().getBlockState(trapdoor);
        ((TrapDoorBlock)state.getBlock()).toggle(state, mob.level(), trapdoor, null);
    }
}
