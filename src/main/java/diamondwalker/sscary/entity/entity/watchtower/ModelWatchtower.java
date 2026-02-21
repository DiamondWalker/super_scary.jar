package diamondwalker.sscary.entity.entity.watchtower;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import diamondwalker.sscary.SScary;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelWatchtower extends EntityModel<EntityWatchtower> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "watchtower"), "main");
    private final ModelPart bb_main;

    public ModelWatchtower(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -286.0F, -6.0F, 12.0F, 272.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(-17.0F, -300.0F, -17.0F, 34.0F, 7.0F, 34.0F, new CubeDeformation(0.0F))
                .texOffs(48, 82).addBox(-11.0F, -14.0F, -11.0F, 22.0F, 7.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(48, 41).addBox(-17.0F, -7.0F, -17.0F, 34.0F, 7.0F, 34.0F, new CubeDeformation(0.0F))
                .texOffs(48, 111).addBox(-11.0F, -293.0F, -11.0F, 22.0F, 7.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void setupAnim(EntityWatchtower entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        bb_main.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
