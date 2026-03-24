package diamondwalker.sscary.entity.entity.unauthorized;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class UnauthorizedRenderer extends EntityRenderer<EntityUnauthorized> {
    public UnauthorizedRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityUnauthorized entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
