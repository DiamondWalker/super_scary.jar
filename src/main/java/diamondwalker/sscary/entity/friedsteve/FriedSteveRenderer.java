package diamondwalker.sscary.entity.friedsteve;

import diamondwalker.sscary.TWAIS;
import diamondwalker.sscary.entity.corrupted.EntityCorrupted;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FriedSteveRenderer extends HumanoidMobRenderer<EntityFriedSteve, HumanoidModel<EntityFriedSteve>> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "textures/entity/fried_steve.png");

    public FriedSteveRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFriedSteve entity) {
        return TEXTURE_LOCATION;
    }
}
