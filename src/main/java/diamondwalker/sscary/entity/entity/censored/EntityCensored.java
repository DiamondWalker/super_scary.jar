package diamondwalker.sscary.entity.entity.censored;

import diamondwalker.sscary.ai.ImprovedMeleeAttackGoal;
import diamondwalker.sscary.ai.TargetOrDespawnGoal;
import diamondwalker.sscary.data.client.ClientData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.Nullable;

public class EntityCensored extends Monster {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EntityCensored.class, EntityDataSerializers.BYTE);

    public long seed = random.nextLong();
    private boolean sentMessage = false;
    private int time = random.nextInt(20 * 15 + 1) + 20 * 30;

    public EntityCensored(EntityType<? extends EntityCensored> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ImprovedMeleeAttackGoal(this, 2.0, true));
        this.targetSelector.addGoal(1, new TargetOrDespawnGoal<>(this, Player.class, false, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (time-- <= 0) discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (random.nextInt(40) == 0) seed = random.nextLong();
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is false.
     */
    public void setClimbing(boolean climbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);

        if (!sentMessage && target instanceof ServerPlayer) {
            if (random.nextBoolean()) {
                StringBuilder builder = new StringBuilder();
                int words = random.nextInt(3, 9);

                while (words > 0) {
                    builder.append("*".repeat(random.nextInt(1, 9)));
                    words--;
                    builder.append(words > 0 ? ' ' : '.');
                }

                MinecraftServer server = getServer();
                if (server != null) {
                    getServer().getPlayerList().broadcastSystemMessage(
                            Component.literal(builder.toString()), false
                    );
                }
            }
            sentMessage = true;
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("messaged", sentMessage);
        compound.putInt("timeLeft", time);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        sentMessage = compound.getBoolean("messaged");
        if (compound.contains("timeLeft")) time = compound.getInt("timeLeft");
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();

        if (level().isClientSide()) {
            ClientData.get().unauthorized = this;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                .add(Attributes.FOLLOW_RANGE, 200)
                .add(Attributes.ATTACK_DAMAGE, 2000)
                .add(Attributes.STEP_HEIGHT, 3)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1.0f);
    }
}
