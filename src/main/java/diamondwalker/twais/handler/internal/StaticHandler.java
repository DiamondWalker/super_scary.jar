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
    private static final SimpleSoundInstance SOUND = SimpleSoundInstance.forUI(TWAISSounds.STATIC.value(), 1.0f, 1.0f);

    @SubscribeEvent
    private static void onOpenWorld(ClientTickEvent.Post event) {
        // TODO: actually test this
        StaticData data = ClientData.get().staticData;
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if (data != null && data.shouldPlaySound()) {
            if (!soundManager.isActive(SOUND)) soundManager.play(SOUND);
        } else {
            if (soundManager.isActive(SOUND)) soundManager.stop(SOUND);
        }
    }
}
