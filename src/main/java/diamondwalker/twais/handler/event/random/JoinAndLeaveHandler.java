package diamondwalker.twais.handler.event.random;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.ChatUtil;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class JoinAndLeaveHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(WorldData.RARE_CHANCE) == 0) {
                ScriptBuilder builder = new ScriptBuilder(server, "join-leave");

                Component join = ChatUtil.getJoinMessage("");
                Component leave = ChatUtil.getLeaveMessage("");
                boolean isJoin = true;
                boolean duplicate = false;

                for (int i = 0; i < 40; i++) {
                    builder.chatMessageForAll(isJoin ? join : leave);

                    float progress = ((float) i) / 40;
                    int interval = (int) (Math.pow(1.0f - progress, 4) * 120);
                    builder.rest(Math.max(1, interval));

                    if (duplicate) {
                        duplicate = false;
                    } else {
                        isJoin = !isJoin;
                        duplicate = progress > 0.4f && random.nextInt(4) == 0;
                    }
                }

                builder.startScript();

                data.eventCooldown();
            }
        }
    }
}
