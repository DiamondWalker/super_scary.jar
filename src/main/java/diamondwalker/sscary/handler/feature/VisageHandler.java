package diamondwalker.sscary.handler.feature;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.entity.player.PlayerData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.entity.entity.visage.EntityVisage;
import diamondwalker.sscary.network.VisageFlashPacket;
import diamondwalker.sscary.registry.SScaryDataAttachments;
import diamondwalker.sscary.registry.SScaryEntities;
import diamondwalker.sscary.registry.SScarySounds;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.IOException;

@EventBusSubscriber
public class VisageHandler {
    private static boolean DELETE_WORLD = false;
    private static final int FOG_FADE_TIME = 160;

    private static boolean scareHappening = false;
    private static boolean readyToClose = false;
    private static SoundInstance scareSound;

    @SubscribeEvent
    private static void handlePlayerHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.hasData(SScaryDataAttachments.PLAYER)) {
            PlayerData data = player.getData(SScaryDataAttachments.PLAYER);
            if (data.visageHealDisable) {
                if (player.level().getEntitiesOfClass(EntityVisage.class, player.getBoundingBox().inflate(200)).isEmpty()) {
                    data.visageHealDisable = false;
                } else {
                    event.setCanceled(true);
                    if (data.healFlashCooldown <= 0) {
                        PacketDistributor.sendToPlayer(player, new VisageFlashPacket(true));
                        data.healFlashCooldown = 20;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    private static void handleFlashCooldown(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.hasData(SScaryDataAttachments.PLAYER)) {
            PlayerData data = player.getData(SScaryDataAttachments.PLAYER);
            if (data.healFlashCooldown > 0) data.healFlashCooldown--;
        }
    }

    @SubscribeEvent
    private static void handleFogColor(ViewportEvent.ComputeFogColor event) {
        float fogAmount = getFogAmount(event.getPartialTick());

        if (fogAmount > 0) {
            event.setRed(Mth.lerp(fogAmount, event.getRed(), 0.0f));
            event.setGreen(Mth.lerp(fogAmount, event.getGreen(), 0.0f));
            event.setBlue(Mth.lerp(fogAmount, event.getBlue(), 0.0f));
        }
    }

    @SubscribeEvent
    private static void handleFogDistance(ViewportEvent.RenderFog event) {
        float fogAmount = getFogAmount(event.getPartialTick());

        if (fogAmount > 0) {
            float n1 = EntityVisage.NEAR_FOG_DISTANCE;
            float n2 = event.getNearPlaneDistance();
            float f1 = EntityVisage.FAR_FOG_DISTANCE;
            float f2 = event.getFarPlaneDistance();
            event.setNearPlaneDistance(Mth.lerp(fogAmount, n2, n1));
            event.setFarPlaneDistance(Mth.lerp(fogAmount, f2, f1));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    private static void handleFogTick(ClientTickEvent.Post event) {
        ClientData data = ClientData.get();

        if (data.isVisageActive()) {
            if (data.visageFogAmount < FOG_FADE_TIME) data.visageFogAmount++;
        } else {
            if (data.visageFogAmount > 0) data.visageFogAmount--;
        }
    }

    @SubscribeEvent
    private static void handleVisageSpawnTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (!level.isClientSide()) {
            WorldData data = WorldData.get(player.getServer());

            if (!data.progression.hasBeenAngered()) return;

            if (player.isAlive() && level.dimension() == Level.OVERWORLD && level.getBrightness(LightLayer.SKY, player.blockPosition()) <= 0) {
                if (player.getY() < 0) data.visage.spawnTicks++;
            } else {
                data.visage.spawnTicks = 0;
            }
            if (SScary.DEV_MODE) System.out.println("VISAGE: " + data.visage.spawnTicks);
        }
    }

    private static float getFogAmount(double partialTicks) {
        ClientData data = ClientData.get();

        double fogAmount = data.visageFogAmount;
        if (data.isVisageActive()) {
            fogAmount += partialTicks;
        } else {
            fogAmount -= partialTicks;
        }

        return (float)Mth.clamp(fogAmount / FOG_FADE_TIME, 0.0, 1.0);
    }

    public static void spawnVisage(MinecraftServer server) {
        new ScriptBuilder(server)
                .action((serv) -> {
                    for (ServerPlayer player : serv.getPlayerList().getPlayers()) {
                        player.connection.send(new ClientboundSoundPacket(SScarySounds.VISAGE_SPAWN, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 0.6F, 1.0F, server.overworld().getRandom().nextLong()));
                    }
                })
                .rest(20 * 10)
                .popupMessageForAll(Component.literal("You should probably run now."))
                .action((serv) -> {
                    for (ServerPlayer player : serv.getPlayerList().getPlayers()) {
                        if (player.isAlive()) {
                            EntityVisage visage = SScaryEntities.VISAGE.get().create(player.level());

                            RandomSource random = player.getRandom();
                            for (int i = 0; i < 10; i++) {
                                Vec3 pos = player.position();
                                double angle = random.nextDouble() * Math.PI * 2;
                                pos = pos.add(Math.cos(angle) * 30, player.getEyeHeight() - visage.getBbHeight() / 2, Math.sin(angle) * 30);
                                visage.setPos(pos);

                                if (!player.hasLineOfSight(visage)) {
                                    player.level().addFreshEntity(visage);
                                    return;
                                }
                            }
                        }
                    }
                })
                .startScript();
    }

    public static void eraseWorld(MinecraftServer server) {
        DELETE_WORLD = true;
        server.halt(false);
    }

    @SubscribeEvent
    private static void deleteWorld(ServerStoppedEvent event) {
        if (DELETE_WORLD) {
            DELETE_WORLD = false;
            try (LevelStorageSource.LevelStorageAccess levelstoragesource$levelstorageaccess = Minecraft.getInstance().getLevelSource().createAccess(event.getServer().storageSource.getLevelId())) {
                levelstoragesource$levelstorageaccess.deleteLevel();
            } catch (IOException ioexception) {
                SScary.LOGGER.warn("Visage level delete failed.", ioexception);
            }
        }
    }

    @SubscribeEvent
    private static void handleScareTick(ClientTickEvent.Pre event) {
        if (scareHappening) {
            if (readyToClose && scareSound != null) { // the sound has started
                if (!Minecraft.getInstance().getSoundManager().isActive(scareSound)) Minecraft.getInstance().stop(); // the sound is over, close the game
            } else {
                if (scareSound != null && Minecraft.getInstance().getSoundManager().isActive(scareSound)) { // the sound is playing, so we're ready
                    readyToClose = true;
                } else { // the sound hasn't started
                    scareSound = SimpleSoundInstance.forUI(SScarySounds.VISAGE_SCARE.value(), 1.0f, 15.0f); // TODO: make this louder i think
                    Minecraft.getInstance().getSoundManager().play(scareSound);
                }
            }
        }
    }

    public static void doMenuScare() {
        scareHappening = true;
    }
}
