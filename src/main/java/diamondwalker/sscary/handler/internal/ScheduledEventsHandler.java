package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.data.server.WorldData;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.function.Consumer;

@EventBusSubscriber
public class ScheduledEventsHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        data.progression.incrementTime();

        for (Consumer<MinecraftServer> action : data.scripts.tickAndGetRemovedScripts()) {
            action.accept(server);
        }
    }
}
