package diamondwalker.twais.handler.event.random.uncommon;

import diamondwalker.twais.Config;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class TossedAroundHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(Config.UNCOMMON_EVENT_CHANCE.getAsInt()) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    int duration = 60 + random.nextInt(140 + 1);
                    ScriptBuilder builder = new ScriptBuilder(server);
                    for (int i = 0; i < duration; i++) {
                        builder.rest(1);
                        builder.action((serv) -> {
                            double x = random.nextDouble() * 2 - 1;
                            double y = random.nextDouble() * 0.6;
                            double z = random.nextDouble() * 2 - 1;
                            Vec3 motion = new Vec3(x, y, z).scale(0.25);
                            player.addDeltaMovement(motion);
                            player.hurtMarked = true;
                        });
                    }
                    data.eventCooldown();
                    builder.startScript();
                }
            }
        }
    }
}
