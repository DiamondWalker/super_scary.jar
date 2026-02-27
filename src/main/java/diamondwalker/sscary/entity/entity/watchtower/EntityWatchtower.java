package diamondwalker.sscary.entity.entity.watchtower;

import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.Queue;

public class EntityWatchtower extends Monster { // TODO: maybe the eye stunning thing?
    private static final EntityDataAccessor<Integer> DESPAWN_TICKS = SynchedEntityData.defineId(EntityWatchtower.class, EntityDataSerializers.INT);

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

    private final AnimatedSpriteHelper.SpriteAnimation winceAnimation = eyeAnimationHelper.defineAnimation()
            .addFrame(0, 4, 1)
            .build();

    public EntityWatchtower(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
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
        if (!level().isClientSide()) {
            if (isAlive() && (level().isDay() || isDespawning())) {
                setDespawnTicks(getDespawnTicks() + 1);

                if (isDespawning()) {
                    boolean despawn = true;
                    for (Player player : getServer().getPlayerList().getPlayers()) {
                        if (player.level() == this.level()) {
                            if (player.distanceTo(this) > 128) continue;

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
                    }

                    if (despawn) this.discard();
                }
            }
        }

        super.aiStep();

        if (!isDespawning()) { // not despawning
            if (eyeAnimationHelper.getCurrentAnimation() != blinkAnimation) eyeAnimationHelper.setAnimation(blinkAnimation);

            for (Player player : level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(128))) {
                if (EntityUtil.hasLongLineOfSight(this, player, 128) && canAttack(player)) {
                    if (!level().isClientSide()) {
                        player.hurt(SScaryDamageTypes.migraine(player, this), 3); // custom source
                    } else {
                        eyeAnimationHelper.setAnimation(null);
                        player.lookAt(EntityAnchorArgument.Anchor.EYES, this.getEyePosition());
                    }
                }
            }
        }
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DESPAWN_TICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("dayDespawnTicks", getDespawnTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setDespawnTicks(compound.getInt("dayDespawnTicks"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
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
