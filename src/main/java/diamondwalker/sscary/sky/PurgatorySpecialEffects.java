package diamondwalker.sscary.sky;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public class PurgatorySpecialEffects extends DimensionSpecialEffects {
    public PurgatorySpecialEffects() {
        super(Float.NaN, true, DimensionSpecialEffects.SkyType.NONE, false, true);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
        return fogColor;
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return true;
    }
}
