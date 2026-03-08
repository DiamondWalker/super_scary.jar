package diamondwalker.sscary.randomevent.uncommon;

import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemDropEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
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

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.UNCOMMON;
    }
}
