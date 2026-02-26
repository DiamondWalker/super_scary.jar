package diamondwalker.sscary.entity.entity.construct;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.ColorOverlayData;
import diamondwalker.sscary.entity.entity.watchtower.EntityWatchtower;
import diamondwalker.sscary.util.shader.EnumShaderLayer;
import diamondwalker.sscary.util.shader.PostProcessingShader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EntityConstruct extends Monster {
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false) {
            @Override
            protected int getAttackInterval() {
                return 0;
            }
        });
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(EntityConstruct.class, EntityDataSerializers.INT);

    public EntityConstruct(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (level().isClientSide()) {
            Player player = Minecraft.getInstance().player;
            if (player != null && entityData.get(TARGET) == Minecraft.getInstance().player.getId()) {
                player.setXRot(player.getXRot() + (player.getRandom().nextFloat() - 0.5f) * 8);
                player.setYRot(player.getYRot() + (player.getRandom().nextFloat() - 0.5f) * 8);
                if (tickCount % 3 == 0) ClientData.get().colorOverlay = new ColorOverlayData(1.0f, 0.0f, 0.0f, random.nextFloat() * 0.5f, 20);
            }
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        this.entityData.set(TARGET, target != null ? target.getId() : -1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TARGET, -1);
    }

    /*@Override
    public void onAddedToLevel() {
        super.onAddedToLevel();

        if (level().isClientSide()) {
            ClientData.get().constructs.add(this);
        }
    }*/

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 7);
    }
}
