package diamondwalker.twais.entity;

import diamondwalker.twais.handler.feature.VisageHandler;
import diamondwalker.twais.network.VisageFlashPacket;
import diamondwalker.twais.registry.TWAISDataAttachments;
import diamondwalker.twais.registry.TWAISSounds;
import diamondwalker.twais.util.EntityUtil;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.RandomStringUtils;

public class EntityVisage extends Entity {
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
        if (player == null || !player.isAlive() /*|| level().getBrightness(LightLayer.SKY, player.blockPosition()) > 0*/) {
            this.kill();
            return;
        }

        boolean isFree = this.level().noCollision(new AABB(EntityUtil.getEntityCenter(this), EntityUtil.getEntityCenter(this)));

        if (!level().isClientSide()) {
            if (isFree) setChaseTicks(getChaseTicks() + 1);

            if (teleportCooldown > 0) {
                teleportCooldown--;
            } else if (tickCount % 4 == 0 && player instanceof ServerPlayer serverPlayer) {
                if (EntityUtil.isPlayerLookingAt(serverPlayer, this)) {
                    teleportCounter++;
                    PacketDistributor.sendToPlayer(serverPlayer, new VisageFlashPacket(false));
                    if (teleportCounter >= 4) {
                        // teleport
                        teleportCounter = 0;
                        teleportCooldown = 100;
                        this.entityData.set(CHASE_TICKS, 0);
                    }
                } else if (teleportCounter > 0) {
                    teleportCounter--;
                }
            }
        }

        float speed;
        speed = 0.0025f * getChaseTicks();
        if (!isFree) {
            speed /= 2;
        }
        Vec3 targetPos = player.getEyePosition();
        Vec3 thisPos = this.position().add(0, this.getBbHeight() / 2, 0);
        setDeltaMovement(targetPos.subtract(thisPos).normalize().scale(speed));
        this.setPos(this.position().add(getDeltaMovement()));

        if (!level().isClientSide() && this.getBoundingBox().intersects(player.getBoundingBox())) {
            if (player.hurt(this.damageSources().generic(), 4)) {
                player.getData(TWAISDataAttachments.PLAYER).visageHealDisable = true;

                if (player.isDeadOrDying()) {
                    // TODO: improve the name fetching and add a config option
                    String name = System.getProperty("user.name").split(" ")[0];
                    ((ServerPlayer)player).connection.send(new ClientboundDisconnectPacket(Component.literal("Stop hiding behind that screen, " + name + ".")));
                    VisageHandler.eraseWorld(getServer());
                }
            }
        }

        if (!level().isClientSide() && tickCount % 15 == 0) { // TODO: maybe this should loop better and be on the client side
            this.playSound(TWAISSounds.VISAGE_CHASE.value(), 2.0f, 1.0f);
        }
    }

    private void setChaseTicks(int chaseTicks) {
        this.entityData.set(CHASE_TICKS, chaseTicks);
    }

    private int getChaseTicks() {
        return this.entityData.get(CHASE_TICKS);
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
