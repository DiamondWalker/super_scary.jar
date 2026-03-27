package diamondwalker.sscary.entity.entity.deadeye;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import diamondwalker.sscary.SScary;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DeadeyeRenderer extends HumanoidMobRenderer<EntityDeadeye, ModelDeadeye> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/entity/fried_steve.png");

    public DeadeyeRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelDeadeye(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    protected void setupRotations(EntityDeadeye entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);

        model.shootingRot = entity.getShootingAnimRotation(partialTick);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDeadeye entity) {
        return TEXTURE_LOCATION;
    }
}
