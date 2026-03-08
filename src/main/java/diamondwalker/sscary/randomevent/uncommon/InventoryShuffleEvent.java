package diamondwalker.sscary.randomevent.uncommon;

import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.Random;

public class InventoryShuffleEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        boolean executed = false;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.isAlive()) {
                Collections.shuffle(player.getInventory().items);
                executed = true;
            }
        }

        return executed;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.UNCOMMON;
    }
}
