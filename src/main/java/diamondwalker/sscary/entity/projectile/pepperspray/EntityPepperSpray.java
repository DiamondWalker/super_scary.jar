package diamondwalker.sscary.entity.projectile.pepperspray;

import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
import diamondwalker.sscary.registry.SScaryEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityPepperSpray extends Projectile {
    public EntityPepperSpray(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public EntityPepperSpray(Level level, LivingEntity shooter) {
        this(SScaryEntities.PEPPER_SPRAY.get(), level);
        this.setOwner(shooter);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.06f;
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            level().addParticle(ParticleTypes.SPIT, getX(), getY() + getBbHeight() / 2, getZ(), getDeltaMovement().x / 10, getDeltaMovement().y / 10, getDeltaMovement().z / 10);
        }

        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult))
            this.hitTargetOrDeflectSelf(hitresult);
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWaterOrBubble()) {
            this.discard();
        } else if (this.tickCount > 60) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale(0.99F));
            this.applyGravity();
            this.setPos(d0, d1, d2);
        }
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.getOwner() instanceof EntityFriedSteve friedSteve) {
            friedSteve.pepperSpray();
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
