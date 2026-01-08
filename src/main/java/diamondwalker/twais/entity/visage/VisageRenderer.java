package diamondwalker.twais.entity.visage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import diamondwalker.twais.TWAIS;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class VisageRenderer extends EntityRenderer<EntityVisage> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "textures/entity/visage.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);

    private static final float SCALE = 1.0f;
    private static final int FRAMES = 4;
    private static final int TICKS_PER_FRAME = 2;

    public VisageRenderer(EntityRendererProvider.Context p_173962_) {
        super(p_173962_);
    }

    protected int getBlockLightLevel(EntityVisage p_114087_, BlockPos p_114088_) {
        return 15;
    }

    @Override
    public void render(EntityVisage entity, float p_114081_, float p_114082_, PoseStack p_114083_, MultiBufferSource p_114084_, int p_114085_) {
        int frame = (entity.tickCount / TICKS_PER_FRAME) % FRAMES;
        float u1 = frame * (1.0f / FRAMES);
        float u2 = (frame + 1) * (1.0f / FRAMES);

        p_114083_.pushPose();
        p_114083_.scale(SCALE, SCALE, SCALE);
        p_114083_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        PoseStack.Pose pose = p_114083_.last();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, pose, p_114085_, -1.0f, -1.0f + 0.7f, u1, 1, 1.0f);
        vertex(vertexconsumer, pose, p_114085_, 1.0f, -1.0f + 0.7f, u2, 1, 1.0f);
        vertex(vertexconsumer, pose, p_114085_, 1.0f, 1.0f + 0.7f, u2, 0, 1.0f);
        vertex(vertexconsumer, pose, p_114085_, -1.0f, 1.0f + 0.7f, u1, 0, 1.0f);
        p_114083_.popPose();
        super.render(entity, p_114081_, p_114082_, p_114083_, p_114084_, p_114085_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityVisage p_114482_) {
        return TEXTURE_LOCATION;
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float x, float y, float u, float v, float transparency) {
        consumer
                .addVertex(pose, x * 0.7f, y * 0.7f, 0.0F)
                .setColor(1.0f, 1.0f, 1.0f, transparency)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
