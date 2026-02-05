package diamondwalker.sscary.entity.taker;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import diamondwalker.sscary.SScary;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ModelTaker extends EntityModel<EntityTaker> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "modeltaker"), "main");

	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart leftarm;
	private final ModelPart rightarm;
	private final ModelPart leftleg;
	private final ModelPart rightleg;

	public ModelTaker(ModelPart root) {
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.leftarm = root.getChild("leftarm");
		this.rightarm = root.getChild("rightarm");
		this.leftleg = root.getChild("leftleg");
		this.rightleg = root.getChild("rightleg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -22.0F, -3.0F, 12.0F, 31.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -31.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(54, 69).addBox(-2.5F, -6.5F, -2.25F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(66, 29).addBox(-2.5F, -6.5F, -1.5F, 5.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(66, 40).addBox(-2.5F, -3.5F, -2.25F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(-4.5F, -21.5F, -2.5F, 9.0F, 15.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(48, 29).addBox(-4.5F, -21.5F, -3.25F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(66, 48).addBox(-1.5F, -21.5F, -3.25F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(36, 69).addBox(-1.5F, -7.5F, -3.25F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(72, 55).addBox(-1.5F, -8.5F, -3.25F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(72, 62).addBox(0.5F, -8.5F, -3.25F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(72, 69).addBox(0.5F, -20.5F, -3.25F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(54, 75).addBox(-1.5F, -20.5F, -3.25F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(48, 43).addBox(1.5F, -21.5F, -3.25F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(36, 57).addBox(1.5F, -12.5F, -3.25F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(54, 57).addBox(-4.5F, -12.5F, -3.25F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(48, 21).addBox(-4.0F, -22.5F, -3.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -53.0F, 0.0F));

		PartDefinition leftarm = partdefinition.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(0, 37).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 46.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -50.5F, 0.0F));

		PartDefinition rightarm = partdefinition.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(36, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 46.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -50.5F, 0.0F));

		PartDefinition leftleg = partdefinition.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(24, 37).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 46.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -20.5F, 0.5F));

		PartDefinition rightleg = partdefinition.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(12, 37).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 46.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -20.5F, 0.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityTaker entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		leftarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		rightarm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		leftleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		rightleg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}