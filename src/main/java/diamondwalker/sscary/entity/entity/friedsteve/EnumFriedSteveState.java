package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.util.shader.EnumShaderLayer;
import diamondwalker.sscary.util.shader.PostProcessingShader;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public enum EnumFriedSteveState {
    DARKNESS(true, new PostProcessingShader(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "shaders/post/color.json"), EnumShaderLayer.NO_GUI, true), (shader) -> {
        shader.setUniform("ColorScale", 0.0f, 0.0f, 0.0f);
    }),
    JUMPSCARE(true, new PostProcessingShader(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "shaders/post/color.json"), EnumShaderLayer.EVERYTHING, true), (shader) -> {
        shader.setUniform("ColorScale", 1.5f, 0.0f, 0.0f);
        shader.setUniform("Saturation", 1.5f);
    }),
    SPRAYED(false),
    CHASING(false),
    CAUGHT(true, new PostProcessingShader(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "shaders/post/color.json"), EnumShaderLayer.EVERYTHING, true), (shader) -> {
        shader.setUniform("ColorScale", 1.5f, 0.0f, 0.0f);
        shader.setUniform("Saturation", 1.5f);
    });

    private final PostProcessingShader shader;
    private final Consumer<PostProcessingShader> shaderAction;
    public final boolean isPartOfJumpscare;

    EnumFriedSteveState(boolean isPartOfJumpscare) {
        this(isPartOfJumpscare, null, null);
    }

    EnumFriedSteveState(boolean isPartOfJumpscare, PostProcessingShader shader, Consumer<PostProcessingShader> shaderAction) {
        this.shader = shader;
        this.shaderAction = shaderAction;
        this.isPartOfJumpscare = isPartOfJumpscare;
    }

    public void activateShader() {
        if (shader != null) {
            shader.activate();
            if (shaderAction != null) {
                shaderAction.accept(shader);
            }
        }
    }

    public void deactivateShader() {
        if (shader != null) shader.deactivate();
    }
}
