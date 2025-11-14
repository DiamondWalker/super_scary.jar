package diamondwalker.twais.handler.event.random;

import diamondwalker.twais.data.server.WorldData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Collections;

@EventBusSubscriber
public class TouchGrassHandler {
    @SubscribeEvent
    public static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(190_000) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    ItemStack stack = new ItemStack(Blocks.GRASS_BLOCK.asItem());
                    stack.set(DataComponents.LORE, new ItemLore(Collections.singletonList(Component.literal("You should go touch some"))));
                    player.getInventory().add(stack);
                }
            }
        }
    }
}
