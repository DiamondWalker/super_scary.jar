package diamondwalker.twais.registry;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.item.InventoryBugItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TWAISItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TWAIS.MODID);

    public static final DeferredItem<Item> INVENTORY_BUG = ITEMS.registerItem("inventory_bug", InventoryBugItem::new, new Item.Properties().stacksTo(1));

    /*public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.twais")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> INVENTORY_BUG.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(INVENTORY_BUG.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());*/

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
