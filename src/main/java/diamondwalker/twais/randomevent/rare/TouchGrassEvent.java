package diamondwalker.twais.randomevent.rare;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Blocks;

import java.util.Collections;

public class TouchGrassEvent {
    public static boolean execute(MinecraftServer server) {
        boolean executed = false;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ItemStack stack = new ItemStack(Blocks.GRASS_BLOCK.asItem());
            stack.set(DataComponents.LORE, new ItemLore(Collections.singletonList(Component.literal("You should go touch some"))));
            player.getInventory().add(stack);
            executed = true;
        }

        return executed;
    }
}
