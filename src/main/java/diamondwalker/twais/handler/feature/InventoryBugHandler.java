package diamondwalker.twais.handler.feature;

import diamondwalker.twais.registry.TWAISItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;

@EventBusSubscriber
public class InventoryBugHandler {
    @SubscribeEvent
    private static void handlePlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().isLocalPlayer()) {
            if (event.getEntity().tickCount % 8 == 0) {
                Inventory inventory = event.getEntity().getInventory();

                if (inventory.countItem(TWAISItems.INVENTORY_BUG.asItem()) > 0) {
                    ArrayList<Integer> emptySlots = new ArrayList<>();

                    for (int i = 0; i < inventory.items.size(); i++) {
                        if (inventory.items.get(i).isEmpty()) {
                            emptySlots.add(i);
                        }
                    }

                    if (!emptySlots.isEmpty()) {
                        int slot = emptySlots.get(event.getEntity().getRandom().nextInt(emptySlots.size()));
                        inventory.add(slot, new ItemStack(TWAISItems.INVENTORY_BUG.asItem()));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    private static void handleBlockDrop(BlockDropsEvent event) {
        if (event.getState().is(Blocks.STONE) || event.getState().is(Blocks.DEEPSLATE)) {
            if (event.getBreaker() instanceof Player && event.getBreaker().getRandom().nextInt(125) == 0) {
                Block.popResource(event.getLevel(), event.getPos(), new ItemStack(TWAISItems.INVENTORY_BUG.asItem()));
            }
        }
    }

    @SubscribeEvent
    private static void handleItemPickup(ItemEntityPickupEvent.Post event) {
        Item bugItem = TWAISItems.INVENTORY_BUG.asItem();
        if (!event.getPlayer().isLocalPlayer() && event.getOriginalStack().is(bugItem)) {
            if (event.getPlayer().getInventory().countItem(bugItem) <= 1) {
                event.getPlayer().sendSystemMessage(Component.literal("Your inventory has been infected by an Inventory Bug!"));
            }
        }
    }
}
