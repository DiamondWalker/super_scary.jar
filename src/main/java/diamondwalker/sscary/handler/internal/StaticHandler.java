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
    private static int staticTime = 0;

    @SubscribeEvent
    private static void tickStatic(ClientTickEvent.Post event) {
        StaticData data = ClientData.get().staticData;

        // sound
        if (data != null && data.shouldPlaySound()) {
            if (!isPlaying()) startPlaying();
            // reset it periodically so it's constant
            if (staticTime++ % 50 == 0) {
                stopPlaying();
                startPlaying();
            }
        } else {
            stopPlaying();
        }

        // tick
        if (data != null) {
            data.timeLeft--;
            if (data.timeLeft <= 0) {
                ClientData.get().staticData = null;
            }
        }
    }

    private static boolean isPlaying() {
        return sound != null && Minecraft.getInstance().getSoundManager().isActive(sound);
    }

    private static void startPlaying() {
        if (sound == null) sound = SimpleSoundInstance.forUI(SScarySounds.STATIC.value(), 1.0f, 1.0f);
        Minecraft.getInstance().getSoundManager().play(sound);
    }

    private static void stopPlaying() {
        if (sound != null) Minecraft.getInstance().getSoundManager().stop(sound);
    }
}
