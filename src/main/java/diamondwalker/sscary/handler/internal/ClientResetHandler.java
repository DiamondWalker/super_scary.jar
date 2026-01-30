package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.util.shader.ShaderManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientResetHandler {
    @SubscribeEvent
    private static void onCloseWorld(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientData.reset();
        ShaderManager.removeClientWorldShaders();
    }
}
