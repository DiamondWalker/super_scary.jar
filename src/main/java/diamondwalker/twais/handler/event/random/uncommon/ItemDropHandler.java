package diamondwalker.twais.handler.event.random.uncommon;

import diamondwalker.twais.data.server.WorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class ItemDropHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(WorldData.UNCOMMON_CHANCE) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (player.isAlive()) {
                        int held = player.getInventory().selected;
                        ItemStack stack = player.getInventory().getItem(held);
                        if (!stack.isEmpty()) {
                            player.drop(stack, false, true);
                            player.getInventory().setItem(held, ItemStack.EMPTY);
                            data.eventCooldown();
                        }
                    }
                }
            }
        }
    }
}
