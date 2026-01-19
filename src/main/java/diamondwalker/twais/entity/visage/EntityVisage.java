package diamondwalker.twais.entity.visage;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.handler.feature.VisageHandler;
import diamondwalker.twais.network.StaticScreenPacket;
import diamondwalker.twais.network.VisageFlashPacket;
import diamondwalker.twais.network.VisageFogPacket;
import diamondwalker.twais.registry.TWAISDataAttachments;
import diamondwalker.twais.registry.TWAISSounds;
import diamondwalker.twais.util.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class EntityVisage extends Entity {
    public static final float NEAR_FOG_DISTANCE = 5.0f;
    public static final float FAR_FOG_DISTANCE = 7.0f;

    public static final EntityDataAccessor<Integer> CHASE_TICKS = SynchedEntityData.defineId(EntityVisage.class, EntityDataSerializers.INT);

    int teleportCounter = 0;
    int teleportCooldown = 0;

    public EntityVisage(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public void tick() {
        super.tick();
        Player player = level().getNearestPlayer(this, Double.MAX_VALUE);
        if (!level().isClientSide()) {
            if (player == null || !player.isAlive() || (EntityUtil.isOnSurface(player, false) && player.getY() > level().getSeaLevel())) {
                if (player instanceof ServerPlayer sp) PacketDistributor.sendToPlayer(sp, new VisageFlashPacket(false));
                this.kill();
                return;
            } else {
                //WorldData.get(getServer()).eventCooldown();
            }
        }
        if (player == null) return;

        boolean isFree = this.level().noCollision(new AABB(EntityUtil.getEntityCenter(this), EntityUtil.getEntityCenter(this)));

        if (!level().isClientSide()) {
            if (isFree) {
                setChaseTicks(getChaseTicks() + 1);

                if (teleportCooldown > 0) {
                    teleportCooldown--;
                } else if (tickCount % 4 == 0 && player instanceof ServerPlayer serverPlayer) {
                    if (EntityUtil.isPlayerLookingAt(serverPlayer, this)) { // TODO: when I add the fog, make it so he won't teleport if he's in the fox
                        teleportCounter++;
                        PacketDistributor.sendToPlayer(serverPlayer, new VisageFlashPacket(false));
                        if (teleportCounter >= 4) {
                            teleport(player);
                        }
                    } else if (teleportCounter > 0) {
                        teleportCounter--;
                    }
                }
            }
        }

        float speed;
        speed = 0.06f + 0.0012f * getChaseTicks();

        Vec3 targetPos = player.getEyePosition();
        Vec3 thisPos = this.position().add(0, this.getBbHeight() / 2, 0);
        setDeltaMovement(targetPos.subtract(thisPos).normalize().scale(speed));
        this.setPos(this.position().add(getDeltaMovement()));

        if (!level().isClientSide() && this.getBoundingBox().intersects(player.getBoundingBox())) {
            if (player.hurt(this.damageSources().generic(), 4)) {
                player.getData(TWAISDataAttachments.PLAYER).visageHealDisable = true;

                if (player.isDeadOrDying()) {
                    ((ServerPlayer)player).connection.send(new ClientboundDisconnectPacket(Component.literal("You should start running.")));
                    VisageHandler.eraseWorld(getServer());
                } else {
                    PacketDistributor.sendToPlayer((ServerPlayer)player, new StaticScreenPacket(15));
                    this.teleport(player);
                }
            }
        }

        if (!level().isClientSide() && tickCount % 45 == 0) { // TODO: maybe this should loop better and be on the client side
            this.playSound(TWAISSounds.VISAGE_CHASE.value(), 2.0f, 1.0f);
        }
    }

    private void teleport(Player player) {
        for (int i = 0; i < 10; i++) {
            Vec3 pos = player.position();
            double angle = random.nextDouble() * Math.PI * 2;
            pos = pos.add(Math.cos(angle) * 30, player.getEyeHeight() - this.getBbHeight() / 2, Math.sin(angle) * 30);
            setPos(pos);

            if (!player.hasLineOfSight(this)) break;
        }

        teleportCounter = 0;
        teleportCooldown = 100;
        this.entityData.set(CHASE_TICKS, 0);
    }

    private void setChaseTicks(int chaseTicks) {
        this.entityData.set(CHASE_TICKS, chaseTicks);
    }

    private int getChaseTicks() {
        return this.entityData.get(CHASE_TICKS);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        PacketDistributor.sendToPlayer(serverPlayer, new VisageFogPacket(true));
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        PacketDistributor.sendToPlayer(serverPlayer, new VisageFogPacket(false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CHASE_TICKS, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }
}
