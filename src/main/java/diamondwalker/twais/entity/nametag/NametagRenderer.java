package diamondwalker.twais.entity.nametag;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NametagRenderer extends EntityRenderer<EntityNametag> {
    public NametagRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityNametag p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNametag entity) {
        return null;
    }
}
