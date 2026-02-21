package diamondwalker.sscary.entity.entity.watchtower;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.entity.entity.taker.EntityTaker;
import diamondwalker.sscary.entity.entity.taker.ModelTaker;
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
        VertexConsumer vertexconsumer = buffer.getBuffer(RENDER_TYPE);

        drawCircle(vertexconsumer, poseStack, packedLight, 0, 0, 5);

        for (EntityWatchtower.TowerDust dust : entity.dusts) {
            float time = partialTicks + (entity.tickCount - dust.time);
            float f = time / 60;
            drawCircle(vertexconsumer, poseStack, packedLight, Mth.cos(dust.angle) * f * 5, Mth.sin(dust.angle) * f * 5, 1);
        }

        poseStack.popPose();
    }

    private static final int SUBDIVISIONS = 30;
    private static final float FADE_OUT_START = 0.6f;
    private static void drawCircle(VertexConsumer consumer, PoseStack stack, int packedLight, float x, float y, float size) {
        stack.pushPose();
        stack.translate(x, y, 0);
        PoseStack.Pose pose = stack.last();
        for (int i = 0; i < SUBDIVISIONS; i++) {
            float f1 = (float)i / SUBDIVISIONS;
            float f2 = (float)(i + 1) / SUBDIVISIONS;

            f1 *= Mth.TWO_PI;
            f2 *= Mth.TWO_PI;

            vertex(consumer, pose, packedLight, 0, 0, 0, 0, 0, 1);
            vertex(consumer, pose, packedLight, Mth.cos(f1) * size * FADE_OUT_START, Mth.sin(f1) * size * FADE_OUT_START, 0, 0, 0, 1);
            vertex(consumer, pose, packedLight, Mth.cos(f2) * size * FADE_OUT_START, Mth.sin(f2) * size * FADE_OUT_START, 0, 0, 0, 1);
            vertex(consumer, pose, packedLight, 0, 0, 0, 0, 0, 1);

            vertex(consumer, pose, packedLight, Mth.cos(f1) * size * FADE_OUT_START, Mth.sin(f1) * size * FADE_OUT_START, 0, 0, 0, 1);
            vertex(consumer, pose, packedLight, Mth.cos(f1) * size, Mth.sin(f1) * size, 0, 0, 0, 0);
            vertex(consumer, pose, packedLight, Mth.cos(f2) * size, Mth.sin(f2) * size, 0, 0, 0, 0);
            vertex(consumer, pose, packedLight, Mth.cos(f2) * size * FADE_OUT_START, Mth.sin(f2) * size * FADE_OUT_START, 0, 0, 0, 1);
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

    @Override
    public ResourceLocation getTextureLocation(EntityWatchtower entity) {
        return TEXTURE_LOCATION;
    }
}
