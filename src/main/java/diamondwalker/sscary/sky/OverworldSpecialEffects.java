package diamondwalker.sscary.sky;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.entity.entity.friedsteve.EnumFriedSteveState;
import diamondwalker.sscary.handler.feature.FriedSteveHandler;
import diamondwalker.sscary.script.Script;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Random;

public class OverworldSpecialEffects extends DimensionSpecialEffects.OverworldEffects {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private static final ResourceLocation SUN_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/sun.png");
    private static final ResourceLocation MOON_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/moon.png");

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f frustumMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable skyFogSetup) {
        skyFogSetup.run();
        if (!isFoggy) {
            FogType fogtype = camera.getFluidInCamera();
            if (fogtype != FogType.POWDER_SNOW && fogtype != FogType.LAVA && !MINECRAFT.levelRenderer.doesMobEffectBlockSky(camera)) {
                PoseStack posestack = new PoseStack();
                posestack.mulPose(frustumMatrix);
                Vec3 vec3 = level.getSkyColor(MINECRAFT.gameRenderer.getMainCamera().getPosition(), partialTick);
                float f = (float)vec3.x;
                float f1 = (float)vec3.y;
                float f2 = (float)vec3.z;
                FogRenderer.levelFogColor();
                Tesselator tesselator = Tesselator.getInstance();
                RenderSystem.depthMask(false);
                RenderSystem.setShaderColor(f, f1, f2, 1.0F);
                ShaderInstance shaderinstance = RenderSystem.getShader();
                MINECRAFT.levelRenderer.skyBuffer.bind();
                MINECRAFT.levelRenderer.skyBuffer.drawWithShader(posestack.last().pose(), projectionMatrix, shaderinstance);
                VertexBuffer.unbind();
                RenderSystem.enableBlend();
                float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
                if (afloat != null) {
                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    posestack.pushPose();
                    posestack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    float f3 = Mth.sin(level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
                    posestack.mulPose(Axis.ZP.rotationDegrees(f3));
                    posestack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                    float f4 = afloat[0];
                    float f5 = afloat[1];
                    float f6 = afloat[2];
                    Matrix4f matrix4f = posestack.last().pose();
                    BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                    bufferbuilder.addVertex(matrix4f, 0.0F, 100.0F, 0.0F).setColor(f4, f5, f6, afloat[3]);
                    int i = 16;

                    for (int j = 0; j <= 16; j++) {
                        float f7 = (float)j * (float) (Math.PI * 2) / 16.0F;
                        float f8 = Mth.sin(f7);
                        float f9 = Mth.cos(f7);
                        bufferbuilder.addVertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3])
                                .setColor(afloat[0], afloat[1], afloat[2], 0.0F);
                    }

                    BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
                    posestack.popPose();
                }

                RenderSystem.blendFuncSeparate(
                        GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
                );
                posestack.pushPose();
                float f11 = 1.0F - level.getRainLevel(partialTick);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
                posestack.mulPose(Axis.YP.rotationDegrees(-90.0F));
                posestack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
                Matrix4f matrix4f1 = posestack.last().pose();
                float f12 = 30.0F;
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, SUN_LOCATION);
                BufferBuilder bufferbuilder1 = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                bufferbuilder1.addVertex(matrix4f1, -f12, 100.0F, -f12).setUv(0.0F, 0.0F);
                bufferbuilder1.addVertex(matrix4f1, f12, 100.0F, -f12).setUv(1.0F, 0.0F);
                bufferbuilder1.addVertex(matrix4f1, f12, 100.0F, f12).setUv(1.0F, 1.0F);
                bufferbuilder1.addVertex(matrix4f1, -f12, 100.0F, f12).setUv(0.0F, 1.0F);
                BufferUploader.drawWithShader(bufferbuilder1.buildOrThrow());
                f12 = 20.0F;
                RenderSystem.setShaderTexture(0, MOON_LOCATION);
                int k = level.getMoonPhase();
                int l = k % 4;
                int i1 = k / 4 % 2;
                float f13 = (float)(l + 0) / 4.0F;
                float f14 = (float)(i1 + 0) / 2.0F;
                float f15 = (float)(l + 1) / 4.0F;
                float f16 = (float)(i1 + 1) / 2.0F;
                bufferbuilder1 = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                bufferbuilder1.addVertex(matrix4f1, -f12, -100.0F, f12).setUv(f15, f16);
                bufferbuilder1.addVertex(matrix4f1, f12, -100.0F, f12).setUv(f13, f16);
                bufferbuilder1.addVertex(matrix4f1, f12, -100.0F, -f12).setUv(f13, f14);
                bufferbuilder1.addVertex(matrix4f1, -f12, -100.0F, -f12).setUv(f15, f14);
                BufferUploader.drawWithShader(bufferbuilder1.buildOrThrow());
                float f10 = level.getStarBrightness(partialTick) * f11;
                if (f10 > 0.0F) {
                    RenderSystem.setShaderColor(f10, f10, f10, f10);
                    FogRenderer.setupNoFog();
                    MINECRAFT.levelRenderer.starBuffer.bind();
                    MINECRAFT.levelRenderer.starBuffer.drawWithShader(posestack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
                    VertexBuffer.unbind();
                    skyFogSetup.run();
                }

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
                posestack.popPose();
                RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                double d0 = this.MINECRAFT.player.getEyePosition(partialTick).y - level.getLevelData().getHorizonHeight(level);
                if (d0 < 0.0) {
                    posestack.pushPose();
                    posestack.translate(0.0F, 12.0F, 0.0F);
                    MINECRAFT.levelRenderer.darkBuffer.bind();
                    MINECRAFT.levelRenderer.darkBuffer.drawWithShader(posestack.last().pose(), projectionMatrix, shaderinstance);
                    VertexBuffer.unbind();
                    posestack.popPose();
                }

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.depthMask(true);
            }
        }

        return true;
    }

    @Override
    public void adjustLightmapColors(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {
        if (pixelX == 15 && pixelY == 15) return;

        ClientData data = ClientData.get();

        if (data.friedSteveChaseTint > 0) {
            Vector3f fadeTo = new Vector3f(1.0f, 0.20f, 0.05f);
            boolean active = data.friedSteve != null && data.friedSteve.getState() == EnumFriedSteveState.CHASING;
            float f = ((active ? partialTicks : -partialTicks) + data.friedSteveChaseTint) / FriedSteveHandler.COLOR_FADE_TIME;
            f = Math.clamp(f, 0.0f, 1.0f);
            colors.set(colors.lerp(fadeTo, f));
        }

        for (Script script : ClientData.get().scripts.getScripts()) {
            script.modifyLightmap(level, partialTicks, skyDarken, blockLightRedFlicker, skyLight, pixelX, pixelY, colors);
        }
        //super.adjustLightmapColors(level, partialTicks, skyDarken, blockLightRedFlicker, skyLight, pixelX, pixelY, colors);
    }
}
