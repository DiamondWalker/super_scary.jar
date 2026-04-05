package diamondwalker.sscary.ai.pathfinding;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.JumpControl;

public class InterruptibleJumpControl extends JumpControl {
    public InterruptibleJumpControl(Mob mob) {
        super(mob);
    }

    public boolean isJumping() {
        return jump;
    }

    public void setJumping(boolean jump) {
        this.jump = jump;
    }
}
