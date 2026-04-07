package diamondwalker.sscary.entity.entity.watchtower;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.registry.SScaryDamageTypes;
import diamondwalker.sscary.util.EntityUtil;
import diamondwalker.sscary.util.rendering.AnimatedSpriteHelper;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

public class EntityWatchtower extends Monster {
    private static final EntityDataAccessor<Integer> SPAWN_TICKS = SynchedEntityData.defineId(EntityWatchtower.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DESPAWN_TICKS = SynchedEntityData.defineId(EntityWatchtower.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WINCE_TICKS = SynchedEntityData.defineId(EntityWatchtower.class, EntityDataSerializers.INT);

    protected final Queue<TowerDust> dusts = new LinkedList<>();
    protected final AnimatedSpriteHelper eyeAnimationHelper = new AnimatedSpriteHelper(1, 6);

    private final AnimatedSpriteHelper.SpriteAnimation blinkAnimation = eyeAnimationHelper.defineAnimation()
            .addFrame(0, 0, 200)
            .addFrame(0, 1, 2)
            .addFrame(0, 2, 2)
            .addFrame(0, 3, 2)
            .addFrame(0, 4, 2)
            .addFrame(0, 5, 2)
            .addFrame(0, 4, 2)
            .addFrame(0, 3, 2)
            .addFrame(0, 2, 2)
            .addFrame(0, 1, 2)
            .build();

    private final AnimatedSpriteHelper.SpriteAnimation spawnAnimation = eyeAnimationHelper.defineAnimation()
            .addFrame(0, 5, 8)
            .addFrame(0, 4, 8)
            .addFrame(0, 3, 8)
            .addFrame(0, 2, 8)
            .addFrame(0, 1, 8)
            .nextAnimation(blinkAnimation)
            .build();

    private final AnimatedSpriteHelper.SpriteAnimation winceAnimation = eyeAnimationHelper.defineAnimation()
            .addFrame(0, 3, 1)
            .build();

    private final AnimatedSpriteHelper.SpriteAnimation winceRecoveryAnimation = eyeAnimationHelper.defineAnimation()
            .addFrame(0, 3, 8)
            .addFrame(0, 2, 8)
            .addFrame(0, 1, 8)
            .nextAnimation(blinkAnimation)
            .build();

    private final EntityWatchtowerEye eye;

    public EntityWatchtower(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        eye = new EntityWatchtowerEye(this, getBbWidth() * 4, getBbWidth() * 4);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            if (!isDespawning()) {
                do {
                    dusts.add(new TowerDust(tickCount, random.nextFloat() * 2 + 3.5f, random.nextFloat() * Mth.TWO_PI));
                } while (random.nextInt(2) == 0);
            }
            while (!dusts.isEmpty() && tickCount - dusts.peek().time > TowerDust.MAX_TIME) dusts.remove();

            eyeAnimationHelper.tick();
        }
    }

    @Override
    public void aiStep() {
        eye.setPos(this.position().add(0, getEyeHeight() - eye.getBbHeight() / 2, 0));
        eye.xo = eye.xOld = eye.getX();
        eye.yo = eye.yOld = eye.getY();
        eye.zo = eye.zOld = eye.getZ();

        List<? extends Player> players = level().players()
                .stream()
                .filter(p -> p.distanceToSqr(this) < 160 * 160 && !p.isSpectator())
                .toList();

        if (!level().isClientSide()) {
            if (isAlive() && (level().isDay() || isDespawning())) {
                setDespawnTicks(getDespawnTicks() + 1);

                if (isDespawning()) {
                    boolean despawn = true;
                    for (Player player : players) {
                        double relX = this.getX() - player.getX();
                        double relZ = this.getZ() - player.getZ();
                        double relMag = Math.abs(relX * relX + relZ * relZ);

                        double lookX = player.getLookAngle().x;
                        double lookZ = player.getLookAngle().z;
                        double lookMag = Math.abs(lookX * lookX + lookZ * lookZ);

                        relX /= relMag;
                        relZ /= relMag;

                        lookX /= lookMag;
                        lookZ /= lookMag;

                        double norm = relX * lookX + relZ * lookZ;

                        if (norm > 0) {
                            despawn = false;
                            break;
                        }
                    }

                    if (despawn) this.discard();
                }
            }
        }

        super.aiStep();

        if (!isDespawning()) { // not despawning
            if (getSpawnTicks() < spawnAnimation.getTotalAnimationTicks()) {
                setSpawnTicks(getSpawnTicks() + 1);
                if (eyeAnimationHelper.getCurrentAnimation() != spawnAnimation) eyeAnimationHelper.setAnimation(spawnAnimation);
                return;
            }

            if (getWinceTicks() > 0) {
                if (getWinceTicks() <= winceRecoveryAnimation.getTotalAnimationTicks()) {
                    if (eyeAnimationHelper.getCurrentAnimation() == winceAnimation) {
                        eyeAnimationHelper.setAnimation(winceRecoveryAnimation);
                    }
                } else {
                    if (eyeAnimationHelper.getCurrentAnimation() != winceAnimation) eyeAnimationHelper.setAnimation(winceAnimation);
                }
                setWinceTicks(getWinceTicks() - 1);
                return;
            }

            if (eyeAnimationHelper.getCurrentAnimation() != winceRecoveryAnimation && eyeAnimationHelper.getCurrentAnimation() != blinkAnimation) {
                eyeAnimationHelper.setAnimation(blinkAnimation);
            }

            for (Player player : players) {
                if (canSee(player)) {
                    if (!level().isClientSide()) {
                        if (player.tickCount % 15 == 0 && EntityUtil.lookingAtEye(player, this) > 0.95) {
                            player.hurt(SScaryDamageTypes.migraine(player, this), 2);
                        }
                    } else {
                        eyeAnimationHelper.setAnimation(blinkAnimation);
                        eyeAnimationHelper.setFrame(0, 0);

                        EntityUtil.forcePlayerToLookAt(player, this, 0.15f);
                        ClientData.get().tower = this;
                    }
                }
            }
        }
    }

    public boolean canSee(Player player) {
        return EntityUtil.hasLongLineOfSight(this, player, 160) && canAttack(player);
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public @Nullable PartEntity<?>[] getParts() {
        return new PartEntity[] {eye};
    }

    protected void attackEye(float damage) {
        setWinceTicks(Math.round(damage * 10));
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        AABB boundingBox = super.getBoundingBoxForCulling();
        double eyeY = getEyeY();
        if (boundingBox.maxY < eyeY) {
            boundingBox = boundingBox.setMaxY(eyeY + (eyeY - boundingBox.maxY));
        }
        return boundingBox;
    }

    public void setSpawnTicks(int ticks) {
        this.entityData.set(SPAWN_TICKS, ticks);
    }

    public int getSpawnTicks() {
        return this.entityData.get(SPAWN_TICKS);
    }

    public void setDespawnTicks(int ticks) {
        this.entityData.set(DESPAWN_TICKS, ticks);
    }

    public int getDespawnTicks() {
        return this.entityData.get(DESPAWN_TICKS);
    }

    public boolean isDespawning() {
        return getDespawnTicks() > 0;
    }

    public void setWinceTicks(int ticks) {
        entityData.set(WINCE_TICKS, ticks);
    }

    public int getWinceTicks() {
        return entityData.get(WINCE_TICKS);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPAWN_TICKS, 0);
        builder.define(DESPAWN_TICKS, 0);
        builder.define(WINCE_TICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("spawnTicks", getSpawnTicks());
        compound.putInt("dayDespawnTicks", getDespawnTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setSpawnTicks(compound.getInt("spawnTicks"));
        setDespawnTicks(compound.getInt("dayDespawnTicks"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void setYRot(float yRot) {
        super.setYRot(0.0f);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                //.add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 5);
    }

    protected static class TowerDust {
        protected static final int MAX_TIME = 50;
        protected final int time;
        protected final float dist;
        protected final float angle;

        public TowerDust(int time, float dist, float angle) {
            this.time = time;
            this.dist = dist;
            this.angle = angle;
        }
    }
}
