package diamondwalker.twais.handler.feature;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.data.client.ClientData;
import diamondwalker.twais.data.entity.player.PlayerData;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.entity.visage.EntityVisage;
import diamondwalker.twais.network.VisageFlashPacket;
import diamondwalker.twais.registry.TWAISDataAttachments;
import diamondwalker.twais.registry.TWAISSounds;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.storage.LevelStorageSource;
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

    @SubscribeEvent
    private static void handlePlayerHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.hasData(TWAISDataAttachments.PLAYER)) {
            PlayerData data = player.getData(TWAISDataAttachments.PLAYER);
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
        if (event.getEntity() instanceof ServerPlayer player && player.hasData(TWAISDataAttachments.PLAYER)) {
            PlayerData data = player.getData(TWAISDataAttachments.PLAYER);
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
    private static void handleVisageSpawwnTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (!level.isClientSide()) {
            WorldData data = WorldData.get(player.getServer());
            if (player.isAlive() && level.getBrightness(LightLayer.SKY, player.blockPosition()) <= 0) {
                if (player.getY() < 0) data.visage.spawnTicks++;
            } else {
                data.visage.spawnTicks = 0;
            }
            if (TWAIS.DEV_MODE) System.out.println("VISAGE: " + data.visage.spawnTicks);
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
                        player.connection.send(new ClientboundSoundPacket(TWAISSounds.VISAGE_SPAWN, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 0.6F, 1.0F, server.overworld().getRandom().nextLong()));
                    }
                })
                .rest(20 * 10)
                .popupMessageForAll(Component.literal("You should probably run now."))
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
                TWAIS.LOGGER.warn("Visage level delete failed.", ioexception);
            }
        }
    }
}
