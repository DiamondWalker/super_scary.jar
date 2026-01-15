package diamondwalker.twais.randomevent.uncommon;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;

public class InventoryShuffleEvent {
    public static boolean execute(MinecraftServer server) {
        boolean executed = false;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.isAlive()) {
                Collections.shuffle(player.getInventory().items);
                executed = true;
            }
        }

        return executed;
    }
}
