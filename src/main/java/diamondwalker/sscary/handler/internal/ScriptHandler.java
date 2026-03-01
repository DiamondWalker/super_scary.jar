package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.data.server.WorldData;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class ScriptHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        data.newScripts.tick();
    }

    @SubscribeEvent
    private static void handlePlayerChat(ServerChatEvent event) {
        WorldData data = WorldData.get(event.getPlayer().server);

        data.newScripts.handleChat(event.getPlayer(), event.getMessage().getString());
    }
}
