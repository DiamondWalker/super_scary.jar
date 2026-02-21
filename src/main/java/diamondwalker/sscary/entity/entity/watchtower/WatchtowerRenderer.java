package diamondwalker.sscary.entity.entity.watchtower;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.entity.entity.taker.EntityTaker;
import diamondwalker.sscary.entity.entity.taker.ModelTaker;
import diamondwalker.sscary.util.AnimatedSpriteHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class WatchtowerRenderer extends MobRenderer<EntityWatchtower, ModelWatchtower> {
    private static final RenderType RENDER_TYPE = RenderType.create(
            "color_quads",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            1536,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .createCompositeState(false)
    );

    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/entity/watchtower.png");
    private static final ResourceLocation EYE_TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/entity/watchtower_eye.png");
    private static final RenderType RENDER_TYPE_EYE = RenderType.entityTranslucent(EYE_TEXTURE_LOCATION);

    public WatchtowerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelWatchtower(context.bakeLayer(ModelWatchtower.LAYER_LOCATION)), 1.0f);
    }

    @Override
    protected void scale(EntityWatchtower livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(2, 2, 2);
    }

    @Override
    public void render(EntityWatchtower entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        poseStack.pushPose();
        poseStack.translate(0, entity.getEyeHeight(), 0);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);

        drawCircle(vertexConsumer, poseStack, packedLight, 0, 0, 5, 1.0f);

        for (EntityWatchtower.TowerDust dust : entity.dusts) {
            float time = partialTicks + (entity.tickCount - dust.time);
            float f = time / EntityWatchtower.TowerDust.MAX_TIME;
            if (f > 1.0f) continue;

            float dist = 1.0f - (1.0f - f) * (1.0f - f);
            float alpha = Mth.clamp((1.0f - f) * 2, 0.0f, 1.0f);
            drawCircle(vertexConsumer, poseStack, packedLight, Mth.cos(dust.angle) * dist * dust.dist, Mth.sin(dust.angle) * dist * dust.dist, alpha, alpha);
        }

        AnimatedSpriteHelper helper = new AnimatedSpriteHelper(1, 1);
        float widthAspect = 20.0f / 21;
        float heightAspect = 1.0f;

        widthAspect *= 2;
        heightAspect *= 2;

        PoseStack.Pose pose = poseStack.last();
        VertexConsumer eyeVertexConsumer = buffer.getBuffer(RENDER_TYPE_EYE);
        eyeVertex(eyeVertexConsumer, pose, packedLight, -widthAspect, -heightAspect, helper.u1(), helper.v2(), 1.0f);
        eyeVertex(eyeVertexConsumer, pose, packedLight, widthAspect, -heightAspect, helper.u2(), helper.v2(), 1.0f);
        eyeVertex(eyeVertexConsumer, pose, packedLight, widthAspect, heightAspect, helper.u2(), helper.v1(), 1.0f);
        eyeVertex(eyeVertexConsumer, pose, packedLight, -widthAspect, heightAspect, helper.u1(), helper.v1(), 1.0f);

        poseStack.popPose();
    }

    private static final int SUBDIVISIONS = 30;
    private static final float FADE_OUT_START = 0.6f;
    private static void drawCircle(VertexConsumer consumer, PoseStack stack, int packedLight, float x, float y, float size, float transparency) {
        stack.pushPose();
        stack.translate(x, y, 0);
        PoseStack.Pose pose = stack.last();
        for (int i = 0; i < SUBDIVISIONS; i++) {
            float f1 = (float)i / SUBDIVISIONS;
            float f2 = (float)(i + 1) / SUBDIVISIONS;

            f1 *= Mth.TWO_PI;
            f2 *= Mth.TWO_PI;

            vertex(consumer, pose, packedLight, 0, 0, 0, 0, 0, 1);
            vertex(consumer, pose, packedLight, Mth.cos(f1) * size * FADE_OUT_START, Mth.sin(f1) * size * FADE_OUT_START, 0, 0, 0, transparency);
            vertex(consumer, pose, packedLight, Mth.cos(f2) * size * FADE_OUT_START, Mth.sin(f2) * size * FADE_OUT_START, 0, 0, 0, transparency);
            vertex(consumer, pose, packedLight, 0, 0, 0, 0, 0, 1);

            vertex(consumer, pose, packedLight, Mth.cos(f1) * size * FADE_OUT_START, Mth.sin(f1) * size * FADE_OUT_START, 0, 0, 0, transparency);
            vertex(consumer, pose, packedLight, Mth.cos(f1) * size, Mth.sin(f1) * size, 0, 0, 0, 0);
            vertex(consumer, pose, packedLight, Mth.cos(f2) * size, Mth.sin(f2) * size, 0, 0, 0, 0);
            vertex(consumer, pose, packedLight, Mth.cos(f2) * size * FADE_OUT_START, Mth.sin(f2) * size * FADE_OUT_START, 0, 0, 0, transparency);
        }

        stack.popPose();
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float x, float y, float r, float g, float b, float a) {
        consumer
                .addVertex(pose, x * 0.7f, y * 0.7f, 0.0F)
                .setColor(r, g, b, a)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    private static void eyeVertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float x, float y, float u, float v, float transparency) {
        consumer
                .addVertex(pose, x, y, 0.1F)
                .setColor(1.0f, 1.0f, 1.0f, transparency)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWatchtower entity) {
        return TEXTURE_LOCATION;
    }
}
