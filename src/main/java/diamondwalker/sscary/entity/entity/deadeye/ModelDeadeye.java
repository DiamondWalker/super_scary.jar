package diamondwalker.sscary.entity.entity.deadeye;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class ModelDeadeye extends PlayerModel<EntityDeadeye> {
    public float shootingRot;

    public ModelDeadeye(ModelPart root, boolean slim) {
        super(root, slim);
    }

    @Override
    public void setupAnim(EntityDeadeye entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.rightArm.yRot = Mth.lerp(shootingRot, this.rightArm.yRot, netHeadYaw * (float) (Math.PI / 180.0));
        this.rightArm.xRot = Mth.lerp(shootingRot, this.rightArm.xRot, headPitch * (float) (Math.PI / 180.0) - Mth.HALF_PI);

        //rightArm.xRot += head.xRot - Mth.HALF_PI;//shootingRot * Mth.HALF_PI;
        //rightArm.yRot += head.yRot;//shootingRot * 0.65f * Mth.HALF_PI;

        hat.copyFrom(head);
        rightSleeve.copyFrom(rightArm);
    }
}
