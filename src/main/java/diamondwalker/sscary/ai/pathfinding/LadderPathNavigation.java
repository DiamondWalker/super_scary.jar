package diamondwalker.sscary.ai.pathfinding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class LadderPathNavigation extends GroundPathNavigation {
    public LadderPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new LadderNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    public boolean canUpdatePath() {
        return super.canUpdatePath() || mob.onClimbable();
    }

    @Override
    public void tick() {
        super.tick();

        if (path != null && mob.getMoveControl() instanceof LadderMoveControl moveControl && mob.onClimbable()) {
            if (path.getPreviousNode() != null && !path.isDone()) {
                Node previousNode = path.getPreviousNode();
                Node nextNode = path.getNextNode();
                if (previousNode.x != nextNode.x || previousNode.z != nextNode.z) { // this is the node where we are getting off the ladder
                    moveControl.setClimbing(mob.getY() < nextNode.y);
                } else {
                    moveControl.setClimbing(previousNode.y < nextNode.y); // we are either moving up or down
                }
            }
        }
    }
}
