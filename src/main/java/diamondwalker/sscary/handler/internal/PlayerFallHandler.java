package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.data.entity.player.PlayerData;
import diamondwalker.sscary.registry.SScaryDataAttachments;
import diamondwalker.sscary.util.EntityUtil;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class PlayerFallHandler {
    @SubscribeEvent
    private static void handlePlayerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerData data = player.getData(SScaryDataAttachments.PLAYER.get());
            if (data.fallDisabled || data.fallGracePeriod > 0) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    private static void handlePlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        PlayerData data = player.getData(SScaryDataAttachments.PLAYER.get());
        if (data.fallDisabled) {
            if (!EntityUtil.isFalling(player)) {
                data.fallDisabled = false;
                data.fallGracePeriod = 30;
            }
        } else if (data.fallGracePeriod > 0) {
            data.fallGracePeriod--;
        }
    }

    public static void disableFall(Player player) {
        player.getData(SScaryDataAttachments.PLAYER.get()).fallDisabled = true;
    }
}
