package diamondwalker.twais.entity.bizarrodude;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.registry.TWAISSounds;
import diamondwalker.twais.util.ChatUtil;
import diamondwalker.twais.util.ScriptBuilder;
import diamondwalker.twais.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityBizarroDude extends Mob {
    private static final TargetingConditions LOOK_CONDITION = TargetingConditions.forNonCombat().ignoreLineOfSight();

    private int time;

    public EntityBizarroDude(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
        time = Integer.MAX_VALUE;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide()) {
            if (tickCount > time) this.discard();

            Player player = level().getNearestPlayer(LOOK_CONDITION, this);
            if (player != null && player.isAlive()) {
                double offset = player.getEyeY() - this.getEyeY();
                //offset = -offset;
                this.getLookControl().setLookAt(player.getX(), this.getEyeY() + offset, player.getZ());
            }
        }
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
