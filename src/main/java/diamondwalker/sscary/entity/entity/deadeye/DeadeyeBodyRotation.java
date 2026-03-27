package diamondwalker.sscary.entity.entity.deadeye;

import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class DeadeyeBodyRotation extends BodyRotationControl {
    private final EntityDeadeye mob;
    private boolean shooting = false;

    public DeadeyeBodyRotation(EntityDeadeye mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void clientTick() {
        if (mob.getShooting() != shooting) {
            shooting = mob.getShooting();
            if (shooting) headStableTime = 20; // force head to turn right away
        }
        super.clientTick();
    }

    @Override
    protected void rotateHeadTowardsFront() {
        float temp = mob.yHeadRot;
        /*if (mob.getShooting())*/ mob.yHeadRot -= 0.55f * 90;
        super.rotateHeadTowardsFront();
        mob.yHeadRot = temp;
    }
}
