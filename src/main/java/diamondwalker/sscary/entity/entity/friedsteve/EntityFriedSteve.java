package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.ai.StareAtDistantPlayerGoal;
import diamondwalker.sscary.ai.TargetPlayerAnywhereGoal;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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

public class EntityFriedSteve extends Monster {
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIME_IN_STATE = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<BlockPos> TELEPORT_TO = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> TELEPORTING = SynchedEntityData.defineId(EntityFriedSteve.class, EntityDataSerializers.BOOLEAN);

    public EntityFriedSteve(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FriedSteveTeleportGoal(this, 20, 20, 4));
        this.goalSelector.addGoal(1, new FriedSteveTeleportGoal(this, 60, 20, 1));
        this.goalSelector.addGoal(1, new FriedSteveChaseGoal(this, 1.0f));
        this.goalSelector.addGoal(2, new StareAtDistantPlayerGoal(this));
        this.targetSelector.addGoal(1, new TargetPlayerAnywhereGoal(this));
        //this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
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
            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance( // TODO: this is ugly (and also it plays whenever he gets reloaded)
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

        if (!level().isClientSide()) {
            WorldData.get(getServer()).randomEvents.timeSinceLastEvent = 0; // pause events
        } else {
            // the teleport particle effects
            BlockPos teleportPos = getTeleportingTo();
            if (teleportPos != null) {
                for (int i = 0; i < 3; i++) {
                    Vec3 particlePos = teleportPos.getBottomCenter().add(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5);
                    level().addParticle(
                            ParticleTypes.FLAME,
                            particlePos.x,
                            particlePos.y,
                            particlePos.z,
                            0,
                            random.nextDouble() * 0.125 + 0.1875,
                            0
                    );
                }
            }
        }

        switch (getState()) {
            case DARKNESS -> {
                if (level().isClientSide()) {
                    if (getTimeInState() == 10) Minecraft.getInstance().getSoundManager().queueTickingSound(new FriedSteveJumpscareSoundInstance()); // FIXME: this just blows up your eardrums
                } else {
                    if (getTimeInState() >= 50) setState(EnumFriedSteveState.JUMPSCARE);
                }
            }
            case JUMPSCARE -> {
                if (level().isClientSide()) {
                    if (getTimeInState() % 4 == 0) {
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
                    if (getTimeInState() >= 100) {
                        // TODO: kill
                    }
                }
            }
            case ANGRY -> {
                // TODO: yap
                if (!level().isClientSide()) {
                    if (getTimeInState() >= 170) setState(EnumFriedSteveState.CHASING);
                } else {
                    Gui gui = Minecraft.getInstance().gui;

                    switch (getTimeInState()) {
                        case 0: {
                            gui.resetTitleTimes();
                            gui.setTitle(Component.literal("AAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHH"));
                            break;
                        }
                        case 60: {
                            gui.resetTitleTimes();
                            gui.setTitle(Component.literal("MY EYESSS!!!!!!"));
                            break;
                        }
                        case 110: {
                            gui.resetTitleTimes();
                            gui.setTitle(Component.literal("You bastard!"));
                            break;
                        }
                        case 160: {
                            gui.resetTitleTimes();
                            gui.setTitle(Component.literal("I'LL KILL YOU!!!"));
                            break;
                        }
                    }
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

        setTimeInState(getTimeInState() + 1);
    }

    public void setState(EnumFriedSteveState state) {
        this.entityData.set(STATE, state.ordinal());
        setTimeInState(0);
    }

    public EnumFriedSteveState getState() {
        return EnumFriedSteveState.values()[this.entityData.get(STATE)];
    }

    public void setTimeInState(int ticks) {
        this.entityData.set(TIME_IN_STATE, ticks);
    }

    public int getTimeInState() {
        return this.entityData.get(TIME_IN_STATE);
    }

    public void setTeleportingTo(BlockPos pos) {
        if (pos != null) {
            entityData.set(TELEPORTING, true);
            entityData.set(TELEPORT_TO, pos);
        } else {
            entityData.set(TELEPORTING, false);
        }
    }

    public BlockPos getTeleportingTo() {
        if (isTeleporting()) {
            return entityData.get(TELEPORT_TO);
        } else {
            return null;
        }
    }

    public boolean isTeleporting() {
        return entityData.get(TELEPORTING);
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
        builder.define(TIME_IN_STATE, 0);

        builder.define(TELEPORT_TO, BlockPos.ZERO);
        builder.define(TELEPORTING, false);

        oldState = EnumFriedSteveState.DARKNESS; // default
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("state", entityData.get(STATE));
        compound.putInt("stateTime", entityData.get(TIME_IN_STATE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        entityData.set(STATE, compound.getInt("state"));
        entityData.set(TIME_IN_STATE, compound.getInt("stateTime"));
    }

    public void pepperSpray() {
        if (getState() == EnumFriedSteveState.JUMPSCARE) setState(EnumFriedSteveState.ANGRY);
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
