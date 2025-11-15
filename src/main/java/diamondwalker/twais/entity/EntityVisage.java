package diamondwalker.twais.entity;

import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityVisage extends Entity {
    public static final EntityDataAccessor<Boolean> CHASE_MODE = SynchedEntityData.defineId(EntityVisage.class, EntityDataSerializers.BOOLEAN);
    public float transparency = 1.0f;

    int ticksChasing = 0;

    public EntityVisage(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public void tick() {
        super.tick();
        Player player = level().getNearestPlayer(this, 64.0f);
        if (player != null && player.isAlive()) {
            if (!level().isClientSide() && distanceTo(player) > 16.0f) {
                double angle = Mth.atan2(player.getZ() - this.getZ(), player.getX() - this.getX());
                angle += Math.PI * (-0.5f + random.nextFloat());
                setPos(player.position().add(Math.cos(angle) * 10, 0, Math.sin(angle) * 10));
                this.entityData.set(CHASE_MODE, true);
                ticksChasing = 0;
            }

            setDeltaMovement(player.position().add(0.0f, player.getBbHeight() / 2, 0.0f).subtract(this.position().add(0.0f, this.getBbHeight() / 2, 0.0f))
                    .normalize().scale(0.2f));

            if (this.entityData.get(CHASE_MODE)) {
                if (!level().isClientSide() && ticksChasing++ > 300) {
                    this.entityData.set(CHASE_MODE, false);
                }
            } else {
                setDeltaMovement(getDeltaMovement().scale(-1));
            }
        } else {
            this.kill();
        }
        this.setPos(position().add(getDeltaMovement()));

        if (level().isClientSide()) {
            if (player != null) {
                transparency = 1.0f - (distanceTo(player) - 10) / 6;
                transparency = Mth.clamp(transparency, 0.0f, 1.0f);
            } else {
                transparency = 1.0f;
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CHASE_MODE, true);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }
}
