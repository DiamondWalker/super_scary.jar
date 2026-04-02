package diamondwalker.sscary.ai.pathfinding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class LadderNodeEvaluator extends WalkNodeEvaluator {
    @Override
    public void prepare(PathNavigationRegion level, Mob mob) {
        super.prepare(level, mob);
        this.currentContext = new PathfindingContext(level, mob) {
            @Override
            public PathType getPathTypeFromState(int x, int y, int z) {
                PathType type = super.getPathTypeFromState(x, y, z);
                if (type == PathType.OPEN && this.level().getBlockState(new BlockPos(x, y, z)).getBlock() instanceof LadderBlock) {
                    type = PathType.WALKABLE;
                }
                return type;
            }
        };
    }

    @Override
    public int getNeighbors(Node[] outputArray, Node p_node) {
        int i = super.getNeighbors(outputArray, p_node);
        //PathType pathtype = this.getCachedPathType(p_node.x, p_node.y + 1, p_node.z);
        PathType pathtype1 = this.getCachedPathType(p_node.x, p_node.y, p_node.z);

        double d0 = this.getFloorLevel(new BlockPos(p_node.x, p_node.y, p_node.z));

        for (Direction direction : Direction.Plane.VERTICAL) {
            Node node = this.findAcceptedNode(p_node.x, p_node.y + direction.getStepY(), p_node.z, 1, d0, direction, pathtype1);

            if (this.isNeighborValid(node, p_node)) {
                outputArray[i++] = node;
            }
        }

        return i;
    }
}
