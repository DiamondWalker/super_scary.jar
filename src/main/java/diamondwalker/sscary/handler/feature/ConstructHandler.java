package diamondwalker.sscary.handler.feature;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.entity.entity.construct.EntityConstruct;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.List;

@EventBusSubscriber
public class ConstructHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    private static void preventKill(LivingDeathEvent event) {
        if (event.getSource().getDirectEntity() instanceof EntityConstruct construct) {
            event.setCanceled(true);
            event.getEntity().setHealth(1.0f);
            construct.discard();
        }
    }
}
