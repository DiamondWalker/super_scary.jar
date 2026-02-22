package diamondwalker.sscary.entity.entity.watchtower;

import diamondwalker.sscary.util.rendering.AnimatedSpriteHelper;
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

    public EntityWatchtower(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.baseTick();

        if (level().isClientSide()) {
            do {
                dusts.add(new TowerDust(tickCount, random.nextFloat() * 2 + 3.5f, random.nextFloat() * Mth.TWO_PI));
            } while (random.nextInt(3) == 0);
            while (!dusts.isEmpty() && tickCount - dusts.peek().time > TowerDust.MAX_TIME) dusts.remove();

            eyeAnimationHelper.setAnimation(blinkAnimation);
            eyeAnimationHelper.tick();
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
