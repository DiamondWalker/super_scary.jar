package diamondwalker.twais.handler.feature;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber
public class VillagerNameHandler {
    @SubscribeEvent
    private static void handleVillagerSpawn(EntityJoinLevelEvent event) {
        if (!event.loadedFromDisk() && !event.getLevel().isClientSide() && event.getEntity().getType() == EntityType.VILLAGER) {
            String name = event.getEntity().getRandom().nextInt(20) == 0 ? "Testicle" : "Testificate";
            event.getEntity().setCustomName(Component.literal(name));
        }
    }
}
