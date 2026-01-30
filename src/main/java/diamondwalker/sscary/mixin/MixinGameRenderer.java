package diamondwalker.sscary.mixin;

import diamondwalker.sscary.util.shader.EnumShaderLayer;
import diamondwalker.sscary.util.shader.ShaderManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class)
public class MixinGameRenderer {
    @Final
    @Shadow
    Minecraft minecraft;

    @Inject(method = "close", at = @At("TAIL"))
    private void closeShaders(CallbackInfo ci) {
        ShaderManager.closeShaders();
    }

    @Inject(method = "resize", at = @At("HEAD"))
    private void resizeShaders(int width, int height, CallbackInfo ci) {
        ShaderManager.resize(width, height);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"))
    private void processShadersPreGUI(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        ShaderManager.doPostProcessing(EnumShaderLayer.NO_GUI, deltaTracker.getGameTimeDeltaTicks());
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;"))
    private void processShadersPostGUIExcludingScreen(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (!waitForScreen()) {
            ShaderManager.doPostProcessing(EnumShaderLayer.GUI, deltaTracker.getGameTimeDeltaTicks());
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;drawScreen(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.AFTER))
    private void processShadersPostGUIIncludingScreen(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (waitForScreen()) {
            ShaderManager.doPostProcessing(EnumShaderLayer.GUI, deltaTracker.getGameTimeDeltaTicks());
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
    }

    private boolean waitForScreen() {
        return this.minecraft.screen != null && !this.minecraft.screen.isPauseScreen() && this.minecraft.screen.shouldCloseOnEsc();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void processShadersPostEverything(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        ShaderManager.doPostProcessing(EnumShaderLayer.EVERYTHING, deltaTracker.getGameTimeDeltaTicks());
        this.minecraft.getMainRenderTarget().bindWrite(false);
    }
}
