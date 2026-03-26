package diamondwalker.sscary.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.entity.entity.censored.EntityCensored;
import diamondwalker.sscary.entity.entity.censored.CensoredRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void renderEntityCensorship(EntityRenderer<? extends Entity> instance, Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Operation<Void> original) {
        if (ClientData.get().unauthorized != null && entity != Minecraft.getInstance().cameraEntity && !(entity instanceof EntityCensored)) {
            EntityCensored unauthorized = ClientData.get().unauthorized;
            if (!unauthorized.isRemoved()) {
                if (new Random(unauthorized.seed * entity.getId()).nextInt(3) == 0) {
                    CensoredRenderer.renderCensoredEntity(entity, (EntityRenderDispatcher) (Object)this, partialTick, poseStack, bufferSource);
                    return;
                }
            } else {
                ClientData.get().unauthorized = null;
            }
        }

        original.call(instance, entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
