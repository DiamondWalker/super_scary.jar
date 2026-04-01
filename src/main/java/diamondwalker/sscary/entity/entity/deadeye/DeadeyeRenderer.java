package diamondwalker.sscary.entity.entity.deadeye;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.registry.SScaryItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DeadeyeRenderer extends HumanoidMobRenderer<EntityDeadeye, ModelDeadeye> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/entity/deadeye.png");

    public DeadeyeRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelDeadeye(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    protected void setupRotations(EntityDeadeye entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);

        float f;
        f = (entity.getShooting() ? partialTick : -partialTick) + entity.shootingTime;
        f /= EntityDeadeye.SHOOTING_ANIM_DURATION;

        f = Math.clamp(f, 0, 1);

        float b = (float)Math.atan(8 * (f - 0.5f));
        float b2 = (float)Math.atan(8 * (0 - 0.5f));
        f = b / (-b2);
        f = f / 2 + 0.5f;

        model.shootingRot = Math.clamp(f, 0, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDeadeye entity) {
        return TEXTURE_LOCATION;
    }
}
