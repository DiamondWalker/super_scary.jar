package diamondwalker.sscary.entity.entity.unauthorized;

import diamondwalker.sscary.ai.ImprovedMeleeAttackGoal;
import diamondwalker.sscary.ai.TargetOrDespawnGoal;
import diamondwalker.sscary.entity.entity.WaterWalkingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityUnauthorized extends WaterWalkingEntity {
    public EntityUnauthorized(EntityType<? extends WaterWalkingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ImprovedMeleeAttackGoal(this, 2.0, true));
        this.targetSelector.addGoal(1, new TargetOrDespawnGoal<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.FOLLOW_RANGE, 200)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1.0f);
    }
}
