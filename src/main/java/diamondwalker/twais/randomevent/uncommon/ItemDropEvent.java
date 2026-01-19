package diamondwalker.twais.randomevent.uncommon;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemDropEvent {
    public static boolean execute(MinecraftServer server) {
        boolean executed = false;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.isAlive()) {
                int held = player.getInventory().selected;
                ItemStack stack = player.getInventory().getItem(held);
                if (!stack.isEmpty()) {
                    player.drop(stack, false, true);
                    player.getInventory().setItem(held, ItemStack.EMPTY);
                    executed = true;
                }
            }
        }

        return executed;
    }
}
