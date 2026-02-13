package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.util.shader.EnumShaderLayer;
import diamondwalker.sscary.util.shader.PostProcessingShader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ShaderHandler {
    private static final PostProcessingShader CREEPY_SHADER = new PostProcessingShader(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "shaders/post/scary.json"), EnumShaderLayer.NO_GUI, true);
    private static final PostProcessingShader COLOR_SHADER = new PostProcessingShader(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "shaders/post/color.json"), EnumShaderLayer.GUI, true);

    @SubscribeEvent
    private static void onOpenWorld(ClientPlayerNetworkEvent.LoggingIn event) {
        if (Config.DISPLAY_AMBIENT_SCREEN_EFFECT.get()) CREEPY_SHADER.activate();
    }

    public static void setColorShader(float r, float g, float b) {
        if (r > 0.999f && g > 0.999f && b > 0.999f) {
            COLOR_SHADER.deactivate();
        } else {
            COLOR_SHADER.activate();
            COLOR_SHADER.setUniform("ColorScale", r, g, b);
        }
    }

    // no onExitWorld needed; the shader itself is marked for removal on world close, which is handled in ClientResetHandler
}
