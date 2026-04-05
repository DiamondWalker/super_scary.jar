package diamondwalker.sscary.ai.pathfinding;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

import java.util.Optional;

public class LadderMoveControl extends MoveControl {
    private boolean climbing = false;

    public LadderMoveControl(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (mob.onClimbable() && Math.abs(mob.getY() - wantedY) > 1) {
            if (mob.verticalCollision) {
                // make sure mob doesn't get stuck on a ledge
                mob.getLastClimbablePos().ifPresent((pos) -> {
                    wantedX = pos.getCenter().x;
                    wantedZ = pos.getCenter().z;
                });
                //operation = Operation.MOVE_TO;
            }
            operation = Operation.MOVE_TO;
            super.tick();

            if (climbing) {
                setJumping(true);
            } else {
                mob.setSpeed(0);
                setJumping(false);
            }
        } else {
            super.tick();
        }
    }

    protected void setClimbing(boolean climbing) {
        this.climbing = climbing;
    }

    private void setJumping(boolean jumping) {
        mob.setJumping(jumping);
        if (mob.getJumpControl() instanceof InterruptibleJumpControl jumpControl) jumpControl.setJumping(jumping);
    }
}
