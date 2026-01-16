package diamondwalker.twais.entity.bizarrodude;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.util.Mth;

public class BizarroDudeModel extends PlayerModel<EntityBizarroDude> {
    public BizarroDudeModel(ModelPart root, boolean slim) {
        super(root, slim);
    }

    @Override
    public void setupAnim(EntityBizarroDude entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.head.xRot = -this.head.xRot;//Mth.HALF_PI;
    }
}
