package diamondwalker.twais.handler.event.random;

import diamondwalker.twais.data.server.WorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class FireHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(WorldData.COMMON_CHANCE) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (!player.isOnFire() && player.isAlive()) {
                        player.setRemainingFireTicks(20 + random.nextInt(40 + 1));
                        data.eventCooldown();
                    }
                }
            }
        }
    }
}
