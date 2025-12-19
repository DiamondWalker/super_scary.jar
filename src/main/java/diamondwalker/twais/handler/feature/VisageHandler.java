package diamondwalker.twais.handler.feature;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.data.client.ClientData;
import diamondwalker.twais.data.entity.player.PlayerData;
import diamondwalker.twais.entity.EntityVisage;
import diamondwalker.twais.network.ScreenColorShaderPacket;
import diamondwalker.twais.network.VisageFlashPacket;
import diamondwalker.twais.registry.TWAISDataAttachments;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.ImmediateWindowHandler;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.IOException;

@EventBusSubscriber
public class VisageHandler {
    private static boolean DELETE_WORLD = false;

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
        if (!ClientData.get().visageFog) return;

        event.setRed(0.0f);
        event.setGreen(0.0f);
        event.setBlue(0.0f);
    }

    @SubscribeEvent
    private static void handleFogDistance(ViewportEvent.RenderFog event) {
        if (!ClientData.get().visageFog) return;

        event.setNearPlaneDistance(5.0f);
        event.setFarPlaneDistance(7.0f);
        event.setCanceled(true);
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
