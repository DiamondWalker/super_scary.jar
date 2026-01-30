package diamondwalker.sscary.handler.feature;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber
public class VillagerNameHandler {
    @SubscribeEvent
    private static void handleVillagerSpawn(EntityJoinLevelEvent event) {
        if (!event.loadedFromDisk() && !event.getLevel().isClientSide() && event.getEntity().getType() == EntityType.VILLAGER) {
            if (event.getEntity() instanceof Villager villager) {
                String name = "Testificate";
                if (!villager.isBaby() && villager.getRandom().nextInt(20) == 0) {
                    name = "Testicle";
                }
                event.getEntity().setCustomName(Component.literal(name));
            }
        }
    }
}
