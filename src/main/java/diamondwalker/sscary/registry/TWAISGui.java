package diamondwalker.sscary.registry;

import diamondwalker.sscary.TWAIS;
import diamondwalker.sscary.gui.overlay.FlashOverlay;
import diamondwalker.sscary.gui.overlay.StaticOverlay;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber
public class TWAISGui {
    @SubscribeEvent(priority = EventPriority.LOWEST) // make sure this is lowest priority so the overlays will be above/below all the other GUI
    public static void registerGui(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "static"), new StaticOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "flash"), new FlashOverlay());
    }
}
