package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.handler.internal.ShaderHandler;
import diamondwalker.sscary.registry.SScarySounds;
import diamondwalker.sscary.sound.FriedSteveJumpscareSoundInstance;
import diamondwalker.sscary.util.shader.EnumShaderLayer;
import diamondwalker.sscary.util.shader.PostProcessingShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityFriedSteve extends Monster { // TODO: this guy should pause events
    private PostProcessingShader darknessShader;
    private PostProcessingShader jumpscareShader;

    public static final EntityDataAccessor<Integer> JUMPSCARE = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> CHASING = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.BOOLEAN);

    public EntityFriedSteve(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
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

            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance( // TODO: this is ugly :(
                    SScarySounds.FRIED_STEVE_SPAWN.value().getLocation(),
                    getSoundSource(),
                    3.0f,
                    0.65f,
                    SoundInstance.createUnseededRandom(),
                    false,
                    0,
                    SoundInstance.Attenuation.LINEAR,
                    0.0,
                    0.0,
                    0.0,
                    true
            ));
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
                if (getJumpscareTime() == 140) Minecraft.getInstance().getSoundManager().queueTickingSound(new FriedSteveJumpscareSoundInstance()); // FIXME: this just blows up your eardrums
                if (getJumpscareTime() <= 100) {
                    darknessShader.deactivate();
                    jumpscareShader.activate();
                    jumpscareShader.setUniform("ColorScale", 1.5f, 0.0f, 0.0f);
                    jumpscareShader.setUniform("Saturation", 1.5f);

                    if (tickCount % 4 == 0) {
                        String[] scareLines = new String[] {
                                "Nowhere to run",
                                "Your time is up",
                                "Found you",
                                "No escape",
                                "This is your end",
                                "Say your prayers",
                                "Don't struggle",
                                "Farewell"
                        };
                        Gui gui = Minecraft.getInstance().gui;
                        gui.setTimes(3, 70, 20);
                        gui.setTitle(Component.literal(scareLines[random.nextInt(scareLines.length)]));
                    }
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
        builder.define(JUMPSCARE, 150);
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

    public static String getName(RandomSource random) {
        int length = 4 + random.nextInt(7); // [4, 10]
        StringBuilder nameBuilder = new StringBuilder(length + 4);
        nameBuilder.append("§k");
        for (int i = 0; i < length; i++) nameBuilder.append('-');
        nameBuilder.append("§r");
        return nameBuilder.toString();
    }
}
