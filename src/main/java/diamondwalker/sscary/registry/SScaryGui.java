package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.gui.overlay.ColorOverlay;
import diamondwalker.sscary.gui.overlay.FlashOverlay;
import diamondwalker.sscary.gui.overlay.StaticOverlay;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber
public class SScaryGui {
    @SubscribeEvent(priority = EventPriority.LOWEST) // make sure this is lowest priority so the overlays will be above/below all the other GUI
    public static void registerGui(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "static"), new StaticOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "color_overlay"), new ColorOverlay());
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "flash"), new FlashOverlay());
    }
}
