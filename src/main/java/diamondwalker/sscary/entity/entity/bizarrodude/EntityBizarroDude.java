package diamondwalker.sscary.entity.entity.bizarrodude;

import diamondwalker.sscary.ai.LookAtFarawayPlayerGoal;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityBizarroDude extends Mob {
    private static final TargetingConditions LOOK_CONDITION = TargetingConditions.forNonCombat().ignoreLineOfSight();

    private int time;

    public EntityBizarroDude(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        time = getRandom().nextInt(20 + 1) + 10;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LookAtFarawayPlayerGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide()) {
            if (tickCount > time) {
                leave();
            }
        }
    }

    private void leave() {
        getServer().getPlayerList().broadcastSystemMessage(ChatUtil.getLeaveMessage(getDisplayName().getString()), false);
        this.discard();
    }

    @Override
    public Component getDisplayName() {
        if (Minecraft.getInstance().player == null) return super.getDisplayName();
        return Component.literal(new StringBuilder(Minecraft.getInstance().player.getName().getString()).reverse().toString());
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!level().isClientSide()) leave();
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("DespawnTime")) time = compound.getInt("DespawnTime");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("DespawnTime", time);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                //.add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 5);
    }
}
