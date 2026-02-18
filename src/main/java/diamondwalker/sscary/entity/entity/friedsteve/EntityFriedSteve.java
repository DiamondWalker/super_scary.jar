package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.data.client.ClientData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityFriedSteve extends Mob { // TODO: this guy should pause events
    public static final String[] MESSAGES = new String[] {
            "You skin is so soft. I can't wait to see it rip.",
            "I'll make this quick.",
            "I'm coming for you.",
            "It'll all be over soon.",
            "Don't struggle. It'll hurt more.",
            "Say your prayers.",
            "I'll be seeing you soon.",
            "Your time is up.",
            "This is going to be fun."
    };

    public static final EntityDataAccessor<Boolean> CHASING = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.BOOLEAN);

    public EntityFriedSteve(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);//return false;
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();

        if (level().isClientSide()) {
            ClientData.get().friedSteve = this;
        }
    }

    private void setChaseMode(boolean chasing) {
        this.entityData.set(CHASING, chasing);
    }

    public boolean isChasing() {
        return this.entityData.get(CHASING);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHASING, true);
    }

    public void pepperSpray() {

    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                //.add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 5);
    }

    private static String getName(RandomSource random) {
        int length = 4 + random.nextInt(7); // [4, 10]
        StringBuilder nameBuilder = new StringBuilder(length + 4);
        nameBuilder.append("§k");
        for (int i = 0; i < length; i++) nameBuilder.append('-');
        nameBuilder.append("§r");
        return nameBuilder.toString();
    }
}
