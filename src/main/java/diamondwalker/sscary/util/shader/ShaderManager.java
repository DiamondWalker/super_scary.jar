package diamondwalker.sscary.util.shader;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import diamondwalker.sscary.SScary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class ShaderManager {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    private static final HashMap<PostProcessingShader, PostChain> effectMap = new HashMap<>();

    protected static void addEffect(PostProcessingShader shader) {
        if (shader.isActive()) return;

        try {
            PostChain effect = new PostChain(MINECRAFT.getTextureManager(), MINECRAFT.getResourceManager(), MINECRAFT.getMainRenderTarget(), shader.location);
            effect.resize(MINECRAFT.getWindow().getWidth(), MINECRAFT.getWindow().getHeight());
            if (effectMap.put(shader, effect) != null) {
                throw new IllegalStateException("Shader " + shader.location + " was already in the effect map in spite of being marked inactive. Something must be wrong!");
            }
        } catch (IOException ioexception) {
            SScary.LOGGER.warn("Failed to load shader: {}", shader.location, ioexception);
        } catch (JsonSyntaxException jsonsyntaxexception) {
            SScary.LOGGER.warn("Failed to parse shader: {}", shader.location, jsonsyntaxexception);
        }
    }

    protected static void removeEffect(PostProcessingShader shader) {
        if (!shader.isActive()) return;

        PostChain chain = effectMap.remove(shader);

        if (chain == null) {
            throw new IllegalStateException("Shader " + shader.location + " was not in the effect map in spite of being marked active. Something must be wrong!");
        }

        chain.close();
    }

    protected static void setUniform(PostProcessingShader shader, String uniformName, Consumer<Uniform> action) {
        if (shader.isActive()) {
            PostChain chain = effectMap.get(shader); // I won't add a null check because if the shader is active, it should be guaranteed to be in the map

            AtomicBoolean uniformSet = new AtomicBoolean(false);
            chain.passes.forEach((pass) -> {
                Uniform uniform = pass.getEffect().getUniform(uniformName);
                if (uniform != null) {
                    action.accept(uniform);
                    uniformSet.set(true);
                }
            });
            if (!uniformSet.get()) {
                SScary.LOGGER.warn("Shader " + shader.location + " does not contain uniform " + uniformName);
            }

            return;
        }

        SScary.LOGGER.warn("Could not set uniform " + uniformName + " because shader " + shader.location + " was not active");
    }

    public static void removeClientWorldShaders() {
        List<PostProcessingShader> shadersToRemove = effectMap.keySet().stream().filter((shader) -> shader.removeOnWorldClose).toList();

        for (PostProcessingShader shader : shadersToRemove) {
            shader.deactivate();
        }
    }

    public static void resize(int width, int height) {
        for (PostChain effect : effectMap.values()) effect.resize(width, height);
    }

    public static void closeShaders() {
        for (PostProcessingShader shader : effectMap.keySet()) {
            shader.deactivate();
        }

        effectMap.clear();
    }

    public static void doPostProcessing(EnumShaderLayer layer, float f) {
        float time = RenderSystem.getShaderGameTime();
        for (Map.Entry<PostProcessingShader, PostChain> effect : effectMap.entrySet()) {
            if (effect.getKey().layer == layer) {
                effect.getValue().setUniform("GameTime", time);
                effect.getValue().process(f);
            }
        }
    }
}
