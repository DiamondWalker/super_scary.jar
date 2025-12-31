package diamondwalker.twais.handler.event.random.uncommon;

import diamondwalker.twais.data.server.WorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Collections;

@EventBusSubscriber
public class InventoryShuffleHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(WorldData.UNCOMMON_CHANCE) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (player.isAlive()) {
                        Collections.shuffle(player.getInventory().items);
                        data.eventCooldown();
                    }
                }
            }
        }
    }
}
