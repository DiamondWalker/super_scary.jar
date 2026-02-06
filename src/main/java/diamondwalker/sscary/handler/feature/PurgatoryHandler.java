package diamondwalker.sscary.handler.feature;

import com.mojang.blaze3d.shaders.FogShape;
import diamondwalker.sscary.data.entity.player.PlayerData;
import diamondwalker.sscary.registry.SScaryDataAttachments;
import diamondwalker.sscary.registry.SScaryDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class PurgatoryHandler {
    @SubscribeEvent
    private static void handlePlayerDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().dimension() == SScaryDimensions.PURGATORY_LEVEL_KEY && event.getEntity() instanceof Player player) {
            if (player.getData(SScaryDataAttachments.PLAYER).eternalPurgatory) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    private static void handleFogColor(ViewportEvent.ComputeFogColor event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getData(SScaryDataAttachments.PLAYER).eternalPurgatory && Minecraft.getInstance().level.dimension() == SScaryDimensions.PURGATORY_LEVEL_KEY) {
            event.setRed(0);
            event.setGreen(0);
            event.setBlue(0);
        }
    }

    @SubscribeEvent
    private static void handleFog(ViewportEvent.RenderFog event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getData(SScaryDataAttachments.PLAYER).eternalPurgatory && Minecraft.getInstance().level.dimension() == SScaryDimensions.PURGATORY_LEVEL_KEY) {
            event.setFogShape(FogShape.SPHERE);
            event.setNearPlaneDistance(0);
            event.setFarPlaneDistance(5);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    private static void handleBlocKBreak(BlockEvent.BreakEvent event) {
        cancelBlockEvent(event);
    }

    @SubscribeEvent
    private static void handleBlockPlace(BlockEvent.EntityPlaceEvent event) {
        cancelBlockEvent(event);
    }

    @SubscribeEvent
    private static void handleBlockMultiPlace(BlockEvent.EntityMultiPlaceEvent event) {
        cancelBlockEvent(event);
    }

    private static void cancelBlockEvent(BlockEvent event) {
        if (event.getLevel() instanceof ServerLevel level && level.dimension() == SScaryDimensions.PURGATORY_LEVEL_KEY) {
            ((ICancellableEvent)event).setCanceled(true);
        }
    }
}
