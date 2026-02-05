package diamondwalker.sscary.entity.taker;

import diamondwalker.sscary.SScary;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TakerRenderer extends MobRenderer<EntityTaker, ModelTaker> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/entity/taker.png");

    public TakerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelTaker(context.bakeLayer(ModelTaker.LAYER_LOCATION)), 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTaker entity) {
        return TEXTURE_LOCATION;
    }
}
