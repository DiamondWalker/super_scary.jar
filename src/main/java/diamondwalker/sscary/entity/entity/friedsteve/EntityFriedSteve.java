package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.handler.internal.ShaderHandler;
import diamondwalker.sscary.util.shader.EnumShaderLayer;
import diamondwalker.sscary.util.shader.PostProcessingShader;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

    private PostProcessingShader darknessShader;
    private PostProcessingShader jumpscareShader;

    public static final EntityDataAccessor<Integer> JUMPSCARE = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.INT);
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

            jumpscareShader = new PostProcessingShader(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "shaders/post/color.json"), EnumShaderLayer.EVERYTHING, true);

            darknessShader = new PostProcessingShader(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "shaders/post/color.json"), EnumShaderLayer.NO_GUI, true);
            darknessShader.activate();
            darknessShader.setUniform("ColorScale", 0.0f, 0.0f, 0.0f);
        }
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();

        if (level().isClientSide()) {
            darknessShader.deactivate();
            jumpscareShader.deactivate();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (getJumpscareTime() > 0) {
            if (!level().isClientSide()) {
                setJumpscareTime(getJumpscareTime() - 1);
            } else {
                if (getJumpscareTime() <= 50) {
                    darknessShader.deactivate();
                    jumpscareShader.activate();
                    jumpscareShader.setUniform("ColorScale", 1.5f, 0.0f, 0.0f);
                    jumpscareShader.setUniform("Saturation", 1.5f);
                }
            }

            for (Entity entity : level().getEntities(this, this.getBoundingBox().inflate(50))) {
                if (entity instanceof Player player) {
                    player.setPos(new Vec3(player.xOld, player.yOld, player.zOld));
                    player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
                    player.lookAt(EntityAnchorArgument.Anchor.EYES, this.getEyePosition());
                }
            }
        } else {
            if (level().isClientSide()) {
                darknessShader.deactivate();
                jumpscareShader.deactivate();
            }
        }
    }

    private void setJumpscareTime(int time) {
        this.entityData.set(JUMPSCARE, time);
    }

    public int getJumpscareTime() {
        return this.entityData.get(JUMPSCARE);
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
        builder.define(JUMPSCARE, 100);
        builder.define(CHASING, false);
    }

    public void pepperSpray() {
        setJumpscareTime(0);
        setChaseMode(true);
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
