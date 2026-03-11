package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.ai.StareAtDistantPlayerGoal;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.SScarySounds;
import diamondwalker.sscary.script.BoatExplosionScript;
import diamondwalker.sscary.sound.FriedSteveJumpscareSoundInstance;
import diamondwalker.sscary.util.ChatUtil;
import diamondwalker.sscary.util.shader.EnumShaderLayer;
import diamondwalker.sscary.util.shader.PostProcessingShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.w3c.dom.Attr;

public class EntityFriedSteve extends Monster { // TODO: this guy should pause events
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.INT);
    private int timeInState = 0;

    public EntityFriedSteve(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FriedSteveChaseGoal(this, 1.0f));
        this.goalSelector.addGoal(2, new StareAtDistantPlayerGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean startRiding(Entity entity) {
        if (getState() == EnumFriedSteveState.CHASING && getTarget() != null && entity instanceof VehicleEntity vehicle) {
            MinecraftServer server = getServer();
            LivingEntity target = getTarget();
            Vec3 vel;
            if (target != null) {
                vel = target.position().add(0, target.getBbHeight() / 2, 0).subtract(vehicle.position());
            } else {
                vel = vehicle.position().add(0, vehicle.getBbHeight() / 2, 0).subtract(this.position());
            }
            vel = new Vec3(vel.x, 0, vel.z).normalize().scale(5);
            vehicle.setDeltaMovement(vel.x, 0.5, vel.z);
            vehicle.hurtMarked = true;
            vehicle.noPhysics = true;
            WorldData.get(server).newScripts.startScript(new BoatExplosionScript(this, vehicle, server));
            String whatIsIt;
            if (vehicle instanceof Boat) {
                whatIsIt = "BOAT";
            } else if (vehicle instanceof Minecart cart) {
                whatIsIt = "CART";
            } else {
                whatIsIt = vehicle.getName().getString().toUpperCase();
            }
            server.getPlayerList().broadcastSystemMessage(ChatUtil.getEntityChatMessage(EntityFriedSteve.getName(random), "GET THAT GODDAMN " + whatIsIt + " OUT OF THE WAY!"), false);
        }
        return false;
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();

        if (level().isClientSide()) {
            ClientData.get().friedSteve = this;

            getState().activateShader();
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
            getState().deactivateShader();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        switch (getState()) {
            case DARKNESS -> {
                if (level().isClientSide()) {
                    if (timeInState == 10) Minecraft.getInstance().getSoundManager().queueTickingSound(new FriedSteveJumpscareSoundInstance()); // FIXME: this just blows up your eardrums
                } else {
                    if (timeInState >= 50) setState(EnumFriedSteveState.JUMPSCARE);
                }
            }
            case JUMPSCARE -> {
                if (level().isClientSide()) {
                    if (timeInState % 4 == 0) {
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
                } else {
                    if (timeInState >= 100) {
                        // TODO: kill
                    }
                }
            }
            case ANGRY -> {
                // TODO: yap
                if (!level().isClientSide() && timeInState >= 100) {
                    setState(EnumFriedSteveState.CHASING);
                }
            }
            case CHASING -> {
                // TODO: chase yapping
            }
        }

        if (getState().isPartOfJumpscare) {
            for (Entity entity : level().getEntities(this, this.getBoundingBox().inflate(50))) {
                if (entity instanceof Player player) {
                    player.setPos(new Vec3(player.xOld, player.yOld, player.zOld));
                    player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
                    player.lookAt(EntityAnchorArgument.Anchor.EYES, this.getEyePosition());
                }
            }
        }

        timeInState++;
    }

    public void setState(EnumFriedSteveState state) {
        this.entityData.set(STATE, state.ordinal());
        timeInState = 0;
    }

    public EnumFriedSteveState getState() {
        return EnumFriedSteveState.values()[this.entityData.get(STATE)];
    }

    private EnumFriedSteveState oldState;
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if (key.equals(STATE) && level().isClientSide()) {
            EnumFriedSteveState newState = getState();
            if (oldState != null) oldState.deactivateShader();
            newState.activateShader();
            oldState = getState();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATE, EnumFriedSteveState.DARKNESS.ordinal());
        oldState = EnumFriedSteveState.DARKNESS; // default
    }

    public void pepperSpray() {
        setState(EnumFriedSteveState.ANGRY);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                //.add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.FOLLOW_RANGE, 666)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.5f)
                .add(Attributes.STEP_HEIGHT, 1.5f)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1.0f);
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
