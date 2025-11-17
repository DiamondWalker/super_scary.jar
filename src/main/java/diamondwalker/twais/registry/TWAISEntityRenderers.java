package diamondwalker.twais.registry;

import diamondwalker.twais.entity.VisageRenderer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber
public class TWAISEntityRenderers {
    @SubscribeEvent
    private static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TWAISEntities.VISAGE.get(), VisageRenderer::new);
    }
}
