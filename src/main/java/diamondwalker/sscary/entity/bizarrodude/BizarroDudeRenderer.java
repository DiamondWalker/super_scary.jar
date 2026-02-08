package diamondwalker.sscary.entity.bizarrodude;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BizarroDudeRenderer extends HumanoidMobRenderer<EntityBizarroDude, PlayerModel<EntityBizarroDude>> {
    protected final PlayerModel<EntityBizarroDude> wideModel;
    protected final PlayerModel<EntityBizarroDude> slimModel;

    public BizarroDudeRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f);
        wideModel = this.model;
        slimModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
    }

    @Override
    protected void scale(EntityBizarroDude livingEntity, PoseStack poseStack, float partialTickTime) {
        super.scale(livingEntity, poseStack, partialTickTime);
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
        //poseStack.translate(0, livingEntity.getBbHeight() * 1.05f, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBizarroDude entity) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            return player.getSkin().texture();
        }
        return DefaultPlayerSkin.getDefaultTexture();
    }

    @Override
    public void render(EntityBizarroDude entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (Minecraft.getInstance().player.getSkin().model() == PlayerSkin.Model.SLIM) {
            this.model = slimModel;
        } else {
            this.model = wideModel;
        }

        // trick the game into thinking this guy is named "Dinnerbone" so he renders upside down
        Component name = entity.getCustomName();
        entity.setCustomName(Component.literal("Dinnerbone"));
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        entity.setCustomName(name);
    }
}
