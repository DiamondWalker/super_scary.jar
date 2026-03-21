package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.data.PermanentSaveData;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.script.Script;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class ScriptHandler {
    @SubscribeEvent
    private static void handleClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().isPaused()) return;

        ClientData.get().scripts.tick();
    }

    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        data.newScripts.tick();
    }

    @SubscribeEvent
    private static void handlePlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        WorldData data = WorldData.get(player.server);
        data.newScripts.addNewPlayer(player);
    }

    @SubscribeEvent
    private static void handlePlayerChat(ServerChatEvent event) {
        WorldData data = WorldData.get(event.getPlayer().server);

        for (Script script : data.newScripts.getScripts()) script.handleChatInput(event.getPlayer(), event.getMessage().getString());
    }

    @SubscribeEvent
    private static void handlePlayerBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            WorldData data = WorldData.get(player.getServer());

            for (Script script : data.newScripts.getScripts()) script.handleBlockBreak(player, event.getState(), event.getPos());
        }
    }
}
