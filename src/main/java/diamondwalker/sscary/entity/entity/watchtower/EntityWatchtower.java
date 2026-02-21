package diamondwalker.sscary.entity.entity.watchtower;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.LinkedList;
import java.util.Queue;

public class EntityWatchtower extends Mob {
    protected final Queue<TowerDust> dusts = new LinkedList<>();

    public EntityWatchtower(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (tickCount % 1 == 0) {
            dusts.add(new TowerDust(tickCount, random.nextFloat() * Mth.TWO_PI));
            while (dusts.size() > 50) dusts.remove();
        }
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

    protected class TowerDust {
        protected final int time;
        protected final float angle;

        public TowerDust(int time, float angle) {
            this.time = time;
            this.angle = angle;
        }
    }
}
