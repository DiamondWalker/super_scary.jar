package diamondwalker.sscary.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Sheep.class)
public class MixinSheep {
    @Inject(at = @At("HEAD"), method = "getRandomSheepColor", cancellable = true)
    private static void setRedSheep(RandomSource random, CallbackInfoReturnable<DyeColor> cir) {
        if (random.nextInt(300) == 0) cir.setReturnValue(DyeColor.RED);
    }
}
