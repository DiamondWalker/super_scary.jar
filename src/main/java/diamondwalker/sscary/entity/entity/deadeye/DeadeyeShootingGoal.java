package diamondwalker.sscary.entity.entity.deadeye;

import diamondwalker.sscary.registry.SScaryDamageTypes;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

import java.util.EnumSet;

public class DeadeyeShootingGoal extends Goal {
    private final EntityDeadeye owner;

    private int shotsLeft = 6;
    private int timeSinceShot;
    private final int firingInterval;
    private final int reloadTime;
    private final double startDistSqr;
    private final double maxDistSqr;

    protected DeadeyeShootingGoal(EntityDeadeye owner, int firingTime, int reloadTime, double startDist, double maxDist) {
        this.owner = owner;
        this.firingInterval = firingTime;
        this.reloadTime = reloadTime;
        this.startDistSqr = startDist * startDist;
        this.maxDistSqr = maxDist * maxDist;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // FIXME: he can't shoot you if you're underwater
        return owner.getTarget() != null && owner.distanceToSqr(owner.getTarget()) < startDistSqr && owner.hasLineOfSight(owner.getTarget());
    }

    @Override
    public boolean canContinueToUse() {
        return owner.getTarget() != null && owner.distanceToSqr(owner.getTarget()) < maxDistSqr && owner.hasLineOfSight(owner.getTarget());
    }

    @Override
    public void start() {
        owner.setShooting(true);
        owner.playSound(SScarySounds.DEADEYE_LOAD.value(), 1.5f, 1);
    }

    @Override
    public void stop() {
        owner.setShooting(false);
    }

    @Override
    public void tick() {
        LivingEntity target = owner.getTarget();

        owner.getNavigation().stop();
        owner.getLookControl().setLookAt(target);
        if (owner.shootingTime >= EntityDeadeye.SHOOTING_ANIM_DURATION) {
            timeSinceShot++;
            if (shotsLeft > 0) {
                if (timeSinceShot > firingInterval) {
                    shoot(target);
                    owner.playSound(SScarySounds.DEADEYE_SHOOT.value(), 1.5f, 1);

                    timeSinceShot = 0;
                    shotsLeft--;
                }
            } else {
                if (timeSinceShot > reloadTime) {
                    shotsLeft = 6;
                    timeSinceShot = 0;
                    owner.playSound(SScarySounds.DEADEYE_LOAD.value(), 1.5f, 1);
                }
            }
        }
    }

    private void shoot(LivingEntity target) {
        ServerLevel level = (ServerLevel) owner.level();
        Vec3 rel = target.getEyePosition()
                .subtract(owner.getEyePosition())
                .normalize()
                .add(
                        owner.getRandom().triangle(0.0, 0.075),
                        owner.getRandom().triangle(0.0, 0.075),
                        owner.getRandom().triangle(0.0, 0.075)
                )
                .normalize();

        Vec3 start = owner.getEyePosition();

        Vec3 bodyPos = new Vec3(owner.getX(), owner.getY(0.8), owner.getZ());
        Vec3 lookingAt = owner.calculateViewVector(owner.getXRot(), owner.getYHeadRot()).normalize();
        Vec3 gunParticlePos = bodyPos.add(lookingAt.scale(1.15));
        level.sendParticles(ParticleTypes.SMOKE, gunParticlePos.x, gunParticlePos.y, gunParticlePos.z, 6, 0, 0, 0, 0.02);

        Vec3 end = start.add(rel.scale(128));

        // TODO: there must be a shortcut for this. Look at EntityUtil it could use it too
        boolean hitBlock = false;
        BlockHitResult blockResult = level.clip(
                new ClipContext(
                        start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner
                )
        );

        if (blockResult.getType() != HitResult.Type.MISS) {
            end = blockResult.getLocation();
            hitBlock = true;
        }

        EntityHitResult entityResult = ProjectileUtil.getEntityHitResult(
                owner,
                start,
                end,
                new AABB(
                        Math.min(start.x(), end.x()),
                        Math.min(start.y(), end.y()),
                        Math.min(start.z(), end.z()),
                        (Math.max(start.x(), end.x()) + 1),
                        (Math.max(start.y(), end.y()) + 1),
                        (Math.max(start.z(), end.z()) + 1)
                ),
                e -> e != owner,
                end.distanceTo(start) * end.distanceTo(start)
        );

        if (entityResult != null && entityResult.getType() != HitResult.Type.MISS) {
            entityResult.getEntity().hurt(SScaryDamageTypes.gun(entityResult.getEntity(), owner), 12);
            end = entityResult.getLocation();
            hitBlock = false;
        }

        if (hitBlock) {
            level.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockResult.getBlockPos())),
                    end.x,
                    end.y,
                    end.z,
                    4,
                    0,
                    0,
                    0,
                    0.01
            );
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
