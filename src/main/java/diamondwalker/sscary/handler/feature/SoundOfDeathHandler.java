package diamondwalker.sscary.handler.feature;

import diamondwalker.sscary.data.client.ClientData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;

@EventBusSubscriber
public class SoundOfDeathHandler {
    @SubscribeEvent
    private static void handleSound(PlaySoundEvent event) {
        if (ClientData.get().isSilenced()) event.setSound(null);
    }
}
