package diamondwalker.twais.handler.internal;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.data.client.ClientData;
import diamondwalker.twais.data.client.StaticData;
import diamondwalker.twais.registry.TWAISSounds;
import diamondwalker.twais.util.shader.EnumShaderLayer;
import diamondwalker.twais.util.shader.PostProcessingShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(Dist.CLIENT)
public class StaticHandler {
    private static SimpleSoundInstance sound = null;
    private static int staticTime = 0;

    @SubscribeEvent
    private static void onOpenWorld(ClientTickEvent.Post event) {
        // TODO: actually test this
        StaticData data = ClientData.get().staticData;
        if (data != null) {
            data.timeLeft--;
            if (data.timeLeft <= 0) {
                ClientData.get().staticData = null;
            }
        }

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
    }

    private static boolean isPlaying() {
        return sound != null && Minecraft.getInstance().getSoundManager().isActive(sound);
    }

    private static void startPlaying() {
        if (sound == null) sound = SimpleSoundInstance.forUI(TWAISSounds.STATIC.value(), 1.0f, 1.0f);
        Minecraft.getInstance().getSoundManager().play(sound);
    }

    private static void stopPlaying() {
        if (sound != null) Minecraft.getInstance().getSoundManager().stop(sound);
    }
}
