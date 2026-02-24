package diamondwalker.sscary.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
}
