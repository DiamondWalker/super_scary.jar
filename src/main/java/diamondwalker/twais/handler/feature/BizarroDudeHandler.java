package diamondwalker.twais.handler.feature;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.ChatUtil;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber
public class BizarroDudeHandler {
    @SubscribeEvent
    public static void handlePlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            MinecraftServer server = event.getEntity().getServer();
            WorldData data = WorldData.get(server);
            if (data != null && data.progression.hasBeenAngered()) {
                RandomSource rand = event.getEntity().getRandom();

                if (rand.nextInt(3) == 0) {
                    String name = event.getEntity().getName().getString();
                    name = new StringBuilder(name).reverse().toString();
                    new ScriptBuilder(server)
                            .rest(20 + rand.nextInt(40 + 1))
                            .chatMessageForAll(ChatUtil.getLeaveMessage(name))
                            .startScript();
                }

                // TODO: the sign variation?
            }
        }
    }
}
