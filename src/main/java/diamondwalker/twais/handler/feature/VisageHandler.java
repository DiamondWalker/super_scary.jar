package diamondwalker.twais.handler.feature;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.network.ScreenColorShaderPacket;
import diamondwalker.twais.network.ScreenFlashPacket;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.ImmediateWindowHandler;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.IOException;

@EventBusSubscriber
public class VisageHandler {
    private static boolean DELETE_WORLD = false;

    @SubscribeEvent
    public static void handlePlayerHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            event.setCanceled(true);
            PacketDistributor.sendToPlayer(player, new ScreenFlashPacket(1.0f, 0.0f, 0.0f)); // TODO: add a cooldown to this so it doesn't spam you
        }
    }

    public static void eraseWorld(MinecraftServer server) {
        DELETE_WORLD = true;
        server.halt(false);
    }

    @SubscribeEvent
    public static void deleteWorld(ServerStoppedEvent event) {
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
