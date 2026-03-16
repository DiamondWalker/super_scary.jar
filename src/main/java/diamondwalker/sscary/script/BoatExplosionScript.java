package diamondwalker.sscary.script;

import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
import diamondwalker.sscary.registry.SScaryScripts;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BoatExplosionScript extends Script {
    private final EntityFriedSteve steve;
    private final VehicleEntity boat;

    public BoatExplosionScript(EntityFriedSteve thrower, VehicleEntity boat, MinecraftServer server) {
        super(SScaryScripts.BOAT_EXPLOSION.get(), server);
        this.steve = thrower;
        this.boat = boat;
    }

    @Override
    public void tick() {
        if (boat == null || boat.isRemoved()) {
            end();
        } else {
            Vec3 vec32 = new Vec3(boat.xo, boat.yo, boat.zo);
            Vec3 vec33 = boat.position();
            HitResult hitresult = boat.level().clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, boat));
            if (hitresult.getType() != HitResult.Type.MISS) {
                vec33 = hitresult.getLocation();
            }

            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(
                    boat.level(),
                    boat,
                    vec32,
                    vec33,
                    boat.getBoundingBox().expandTowards(boat.getDeltaMovement()).inflate(1.0),
                    (hitEntity) -> hitEntity != boat && hitEntity != steve
            );
            if (entityhitresult != null) {
                hitresult = entityhitresult;
            }

            if (hitresult.getType() != HitResult.Type.MISS) {
                boat.setPos(hitresult.getLocation());
                boat.level().explode(boat, boat.getX(), boat.getY(), boat.getZ(), 10, Level.ExplosionInteraction.MOB);
                boat.discard();
            }
        }
    }
}
