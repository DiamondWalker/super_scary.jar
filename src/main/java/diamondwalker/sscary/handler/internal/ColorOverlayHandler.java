package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.ColorOverlayData;
import diamondwalker.sscary.data.client.StaticData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber
public class ColorOverlayHandler {
    @SubscribeEvent
    private static void tickOverlay(ClientTickEvent.Post event) {
        ColorOverlayData data = ClientData.get().colorOverlay;

        if (data != null) {
            if (!data.shouldContinue()) {
                ClientData.get().colorOverlay = null;
            }
        }
    }
}
