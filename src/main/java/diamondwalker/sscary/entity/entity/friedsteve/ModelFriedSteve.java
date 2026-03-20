package diamondwalker.sscary.entity.entity.friedsteve;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Function;

public class ModelFriedSteve extends PlayerModel<EntityFriedSteve> {

    public ModelFriedSteve(ModelPart root, boolean slim) {
        super(root, slim);
    }

    @Override
    public void setupAnim(EntityFriedSteve entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if (entity.getState() == EnumFriedSteveState.SPRAYED) {
            float startF = Math.clamp((float)entity.getTimeInState() / 70, 0.0f, 1.0f);
            float endF = Math.clamp((float)(entity.getTimeInState() - 130) / 30, 0.0f, 1.0f);
            float f = Math.min(startF, 1.0f - endF);
            f = (((6.0f*f)-15.0f)*f+10.0f)*f*f*f;

            float leftXRot = leftArm.xRot - 2.398f;
            float leftZRot = leftArm.zRot - 0.897f;
            float leftYRot = leftArm.yRot + 0.19f * (float)Math.sin(ageInTicks * 0.65) - 0.43f;
            leftArm.xRot = Mth.lerp(f, leftArm.xRot, leftXRot);
            leftArm.yRot = Mth.lerp(f, leftArm.yRot, leftYRot);
            leftArm.zRot = Mth.lerp(f, leftArm.zRot, leftZRot);
            leftSleeve.copyFrom(leftArm);

            float rightXRot = rightArm.xRot - 2.398f;
            float rightZRot = rightArm.zRot + 0.897f;
            float rightYRot = rightArm.yRot + 0.19f * (float)Math.cos(ageInTicks * 0.65) + 0.43f;
            rightArm.xRot = Mth.lerp(f, rightArm.xRot, rightXRot);
            rightArm.yRot = Mth.lerp(f, rightArm.yRot, rightYRot);
            rightArm.zRot = Mth.lerp(f, rightArm.zRot, rightZRot);
            rightSleeve.copyFrom(rightArm);

            head.xRot = Mth.lerp(f, head.xRot, 0.0f);
            head.yRot = Mth.lerp(f, head.yRot, 0.0f);
            head.zRot = Mth.lerp(f, head.zRot, 0.0f);
            hat.copyFrom(head);
        }
    }
}
