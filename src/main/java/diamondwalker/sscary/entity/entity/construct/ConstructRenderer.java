package diamondwalker.sscary.entity.entity.construct;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class ConstructRenderer extends HumanoidMobRenderer<EntityConstruct, HumanoidModel<EntityConstruct>> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/entity/construct.png");

    public ConstructRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    protected int getBlockLightLevel(EntityConstruct entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityConstruct entity) {
        return TEXTURE_LOCATION;
    }
}
