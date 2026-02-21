package diamondwalker.sscary.entity.entity.watchtower;

import com.mojang.blaze3d.vertex.PoseStack;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.entity.entity.taker.EntityTaker;
import diamondwalker.sscary.entity.entity.taker.ModelTaker;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WatchtowerRenderer extends MobRenderer<EntityWatchtower, ModelWatchtower> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/entity/watchtower.png");

    public WatchtowerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelWatchtower(context.bakeLayer(ModelWatchtower.LAYER_LOCATION)), 1.0f);
    }

    @Override
    protected void scale(EntityWatchtower livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(2, 2, 2);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWatchtower entity) {
        return TEXTURE_LOCATION;
    }
}
