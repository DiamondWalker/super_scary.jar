package diamondwalker.sscary.handler.feature;

import diamondwalker.sscary.data.client.ClientData;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber
public class FriedSteveHandler {
    public static final int COLOR_FADE_TIME = 25;

    @SubscribeEvent
    private static void handleTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().isPaused()) return;

        ClientData data = ClientData.get();

        if (data.friedSteve != null && data.friedSteve.isRemoved()) data.friedSteve = null;

        if (data.friedSteve != null && data.friedSteve.isChasing()) {
            if (data.friedSteveChaseTint < COLOR_FADE_TIME) data.friedSteveChaseTint++;
        } else {
            if (data.friedSteveChaseTint > 0) data.friedSteveChaseTint--;
        }
    }

    @SubscribeEvent
    private static void handleJumpscareFOV(ComputeFovModifierEvent event) {
        ClientData data = ClientData.get();

        if (data.friedSteve != null && data.friedSteve.getJumpscareTime() > 0) {
            event.setNewFovModifier(event.getNewFovModifier() * (data.friedSteve.getRandom().nextFloat() * 0.4f + 0.8f));
        }
    }
}
