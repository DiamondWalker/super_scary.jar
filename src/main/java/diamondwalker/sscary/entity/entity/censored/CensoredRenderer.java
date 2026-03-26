package diamondwalker.sscary.entity.entity.censored;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public class CensoredRenderer extends EntityRenderer<EntityCensored> {
    private static final RenderType RENDER_TYPE = RenderType.create(
            "unauthorized",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_SHADER)
                    .setTransparencyState(RenderStateShard.TransparencyStateShard.NO_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );

    public CensoredRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityCensored entity, float p_114081_, float partialTick, PoseStack p_114083_, MultiBufferSource p_114084_, int p_114085_) {
        renderCensoredEntity(entity, this.entityRenderDispatcher, partialTick, p_114083_, p_114084_);

        super.render(entity, p_114081_, partialTick, p_114083_, p_114084_, p_114085_);
    }

    public static void renderCensoredEntity(Entity entity, EntityRenderDispatcher entityRenderDispatcher, float partialTick, PoseStack p_114083_, MultiBufferSource p_114084_) {
        AABB box = entity.getBoundingBoxForCulling();
        float width = (float)(box.getXsize());
        width = Mth.sqrt(2 * width * width);
        float height = (float)(box.getYsize());
        width += 0.5f;
        height += 0.5f;
        //width *= 1.2f;
        //height *= 1.2f;

        p_114083_.pushPose();

        p_114083_.translate(0, box.getYsize() / 2, 0);
        p_114083_.mulPose(entityRenderDispatcher.cameraOrientation());
        PoseStack.Pose pose = p_114083_.last();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, pose, -width / 2, -height / 2);
        vertex(vertexconsumer, pose, width / 2, -height / 2);
        vertex(vertexconsumer, pose, width / 2, height / 2);
        vertex(vertexconsumer, pose, -width / 2, height / 2);

        p_114083_.popPose();
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y) {
        consumer
                .addVertex(pose, x, y, 0.0F)
                .setColor(0.0f, 0.0f, 0.0f, 1.0f);
                //setOverlay(OverlayTexture.NO_OVERLAY)
                //setLight(light)
                //setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCensored entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
