package diamondwalker.sscary.util;

import diamondwalker.sscary.data.client.ClientData;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class EntityUtil {
    public static Vec3 getEntityCenter(Entity entity) {
        return entity.position().add(0, entity.getBbHeight() / 2, 0);
    }

    public static boolean hasLongLineOfSight(Entity entityLooking, Entity entityLookedAt) {
        return hasLongLineOfSight(entityLooking, entityLookedAt, Double.MAX_VALUE);
    }

    public static boolean hasLongLineOfSight(Entity entityLooking, Entity entityLookedAt, double maxDistance) {
        if (entityLookedAt.level() != entityLooking.level()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(entityLooking.getX(), entityLooking.getEyeY(), entityLooking.getZ());
            Vec3 vec31 = new Vec3(entityLookedAt.getX(), entityLookedAt.getEyeY(), entityLookedAt.getZ());
            return vec31.distanceTo(vec3) > maxDistance
                    ? false
                    : entityLooking.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entityLooking)).getType() == HitResult.Type.MISS;
        }
    }

    public static boolean isPlayerLookingAt(Player player, Entity entity) {
        Vec3 eyePos = player.getEyePosition();
        double dist = eyePos.distanceTo(getEntityCenter(entity)) + Math.max(entity.getBbWidth(), entity.getBbHeight());

        HitResult blockResult = player.pick(dist, 1.0f, false);
        if (blockResult.getType() != HitResult.Type.MISS) {
            dist = blockResult.getLocation().distanceTo(eyePos);
        }

        Vec3 lookVec = eyePos.add(player.getLookAngle().normalize().scale(dist));
        AABB aabb = player.getBoundingBox().minmax(entity.getBoundingBox());
        EntityHitResult entityResult = ProjectileUtil.getEntityHitResult(
                player, eyePos, lookVec, aabb, p_234237_ -> /*!p_234237_.isSpectator() && p_234237_.isPickable()*/p_234237_ != player, Mth.square(dist)
        );

        return entityResult != null && entityResult.getEntity() == entity;
    }

    public static boolean isOnSurface(Entity entity, boolean includeLeaves) {
        return entity.getBlockY() >= entity.level().getHeight(includeLeaves ? Heightmap.Types.MOTION_BLOCKING : Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, entity.getBlockX(), entity.getBlockZ());
    }

    public static boolean isFalling(LivingEntity entity) {
        return !entity.onGround() && !entity.isInWater() && !entity.onClimbable();
    }

    public static void forcePlayerToLookAt(Player player, LivingEntity lookAt, float multiplier) {
        double d0 = lookAt.getX() - player.getX();
        double d1 = lookAt.getEyeY() - player.getEyeY();
        double d2 = lookAt.getZ() - player.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float newXRot = Mth.wrapDegrees((float)(-(Mth.atan2(d1, d3) * 180.0F / (float)Math.PI)));
        float newYRot = Mth.wrapDegrees((float)(Mth.atan2(d2, d0) * 180.0F / (float)Math.PI) - 90.0F);

        player.turn((newYRot - player.getYRot()) * multiplier / 0.15f, (newXRot - player.getXRot()) * multiplier / 0.15f);
    }

    public static double lookingAtEye(Player player, LivingEntity lookingAt) {
        Vec3 eyeVector = lookingAt.getEyePosition().subtract(player.getEyePosition()).normalize();
        Vec3 lookVector = player.getLookAngle();
        return lookVector.dot(eyeVector);
    }

    public static boolean hasPathTo(Mob entity, LivingEntity target, int testAccuracy) {
        return hasPathTo(entity, target.blockPosition(), testAccuracy);
    }

    public static boolean hasPathTo(Mob entity, BlockPos targetPos, int testAccuracy) {
        Path path = entity.getNavigation().getPath();
        if (path != null) {
            Node node = path.getEndNode();
            if (node != null) {
                BlockPos pathEndPos = node.asBlockPos();
                if (
                        Math.abs(pathEndPos.getX() - targetPos.getX()) <= testAccuracy &&
                                Math.abs(pathEndPos.getY() - targetPos.getY()) <= testAccuracy &&
                                Math.abs(pathEndPos.getZ() - targetPos.getZ()) <= testAccuracy
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static BlockPos findSolidGroundUnder(Entity entity) {
        if (entity.mainSupportingBlockPos.isPresent()) return entity.mainSupportingBlockPos.get();

        Level level = entity.level();
        BlockPos pos = entity.blockPosition();

        while (pos.getY() >= level.getMinBuildHeight() && !level.getBlockState(pos).blocksMotion()) {
            pos = pos.below();
        }

        return pos.above();
    }
}
