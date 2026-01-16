package diamondwalker.twais.entity.bizarrodude;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import diamondwalker.twais.entity.corrupted.EntityCorrupted;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BizarroDudeRenderer extends HumanoidMobRenderer<EntityBizarroDude, BizarroDudeModel> {
    protected final BizarroDudeModel wideModel;
    protected final BizarroDudeModel slimModel;
    public BizarroDudeRenderer(EntityRendererProvider.Context context) {
        super(context, new BizarroDudeModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5f); // FIXME: support for slim players
        wideModel = this.model;
        slimModel = new BizarroDudeModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
    }

    @Override
    protected void scale(EntityBizarroDude livingEntity, PoseStack poseStack, float partialTickTime) {
        super.scale(livingEntity, poseStack, partialTickTime);
        poseStack.scale(1, -1, 1);
        poseStack.translate(0, livingEntity.getBbHeight() * 1.05f, 0);
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
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
