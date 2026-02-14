package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.StaticData;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(Dist.CLIENT)
public class StaticHandler {
    private static SimpleSoundInstance sound = null;

    @SubscribeEvent
    private static void tickStatic(ClientTickEvent.Post event) {
        StaticData data = ClientData.get().staticData;

        if (data != null) {
            data.timeLeft--;
            if (data.timeLeft <= 0) {
                ClientData.get().staticData = null;
            }
        }
    }
}
