package diamondwalker.sscary.entity.corrupted;

import diamondwalker.sscary.TWAIS;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CorruptedRenderer extends HumanoidMobRenderer<EntityCorrupted, HumanoidModel<EntityCorrupted>> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "textures/entity/corrupted.png");

    public CorruptedRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCorrupted entity) {
        return TEXTURE_LOCATION;
    }
}
