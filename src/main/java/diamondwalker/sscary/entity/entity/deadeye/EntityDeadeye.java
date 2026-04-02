package diamondwalker.sscary.entity.entity.deadeye;

import diamondwalker.sscary.ai.ApproachTargetGoal;
import diamondwalker.sscary.ai.BridgeOverWaterGoal;
import diamondwalker.sscary.ai.TargetOrDespawnGoal;
import diamondwalker.sscary.registry.SScaryItems;
import diamondwalker.sscary.sound.ConstructSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityDeadeye extends Monster {
    private static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.defineId(EntityDeadeye.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BUILDING = SynchedEntityData.defineId(EntityDeadeye.class, EntityDataSerializers.INT);
    protected static final int SHOOTING_ANIM_DURATION = 15;
    protected int shootingTime = 0;

    public EntityDeadeye(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.LAVA, getPathfindingMalus(PathType.WATER));
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
    }

    @Override
    public ItemStack getMainHandItem() {
        if (getShooting()) return new ItemStack(SScaryItems.GUN.get());
        return super.getMainHandItem();
    }

    @Override
    public ItemStack getOffhandItem() {
        if (getBuilding() > 0) return new ItemStack(Blocks.COBBLESTONE.asItem());
        return super.getOffhandItem();
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new DeadeyeBodyRotation(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BridgeOverWaterGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        // TODO: make him open trapdoors and maybe even go up ladders?
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, false) {
            @Override
            public void stop() {
                // do nothing; we will not close the door
            }
        });
        this.goalSelector.addGoal(2, new DeadeyeShootingGoal(this, 10, 50, 10, 15));
        this.goalSelector.addGoal(3, new ApproachTargetGoal(this, 1.0f));
        this.targetSelector.addGoal(1, new TargetOrDespawnGoal<>(this, Player.class, false, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHOOTING, false);
        builder.define(BUILDING, 0);
    }

    public void setShooting(boolean shooting) {
        this.entityData.set(SHOOTING, shooting);
    }

    public boolean getShooting() {
        return this.entityData.get(SHOOTING);
    }

    public void setBuilding(int ticks) {
        if (ticks > getBuilding()) swing(InteractionHand.OFF_HAND);
        this.entityData.set(BUILDING, ticks);
    }

    public int getBuilding() {
        return this.entityData.get(BUILDING);
    }

    @Override
    public void tick() {
        super.tick();

        if (getShooting()) {
            if (shootingTime < SHOOTING_ANIM_DURATION) shootingTime++;
        } else {
            if (shootingTime > 0) shootingTime--;
        }

        if (getBuilding() > 0) {
            setBuilding(getBuilding() - 1);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.MOVEMENT_SPEED, 0.22f)
                .add(Attributes.FOLLOW_RANGE, 100);
    }
}
