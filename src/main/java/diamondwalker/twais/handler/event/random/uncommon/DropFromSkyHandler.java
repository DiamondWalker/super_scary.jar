package diamondwalker.twais.handler.event.random.uncommon;

import diamondwalker.twais.Config;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class DropFromSkyHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(Config.UNCOMMON_EVENT_CHANCE.getAsInt()) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (player.isAlive() && !player.getAbilities().flying) {
                        new ScriptBuilder(server)
                                .action((serv) -> player.teleportTo(player.getX(), Math.max(500, player.level().getMaxBuildHeight()), player.getZ()))
                                .rest(1)
                                .action((serv) -> player.fallDistance = -1000)
                                .startScript();
                    }
                    data.eventCooldown();
                }
            }
        }
    }
}
