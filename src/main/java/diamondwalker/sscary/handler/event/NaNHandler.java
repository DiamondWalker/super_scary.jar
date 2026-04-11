package diamondwalker.sscary.handler.event;

import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.script.NaNOffendedScript;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

@EventBusSubscriber
public class NaNHandler {
    @SubscribeEvent
    private static void handleChatMessage(ServerChatEvent event) {
        if (event.getMessage().getString().toLowerCase().matches("(.+)?(go to hell|you suck|i hate you|kill yourself|kys|gtfo|screw you|fuck off|fuck you|asshole|dick|bitch|piece of shit|shut up|stfu|shut the hell up|shut the fuck up)(.+)?")) {
            MinecraftServer server = event.getPlayer().getServer();
            WorldData data = WorldData.get(server);
            if (!data.nan.offended) {
                data.newScripts.startScript(new NaNOffendedScript(event.getPlayer()));
                data.nan.offended = true;
            }
        }
    }
}
