package diamondwalker.twais;

import diamondwalker.twais.data.client.ClientData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Random;

public class OverworldSpecialEffects extends DimensionSpecialEffects.OverworldEffects {
    private final int WACKY_COLOR_TICKS = 2;

    @Override
    public void adjustLightmapColors(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {
        if (pixelX == 15 && pixelY == 15) return;

        ClientData data = ClientData.get();

        if (data.wackyColors) {
            Random thisTickRand = new Random(level.getGameTime() / WACKY_COLOR_TICKS * (pixelX + pixelY + 1));
            Random nextTickRand = new Random((level.getGameTime() / WACKY_COLOR_TICKS + 1) * (pixelX + pixelY + 1));

            float lerp = ((partialTicks + level.getGameTime()) % WACKY_COLOR_TICKS) / WACKY_COLOR_TICKS;
            System.out.println(level.getGameTime());

            Vector3f thisTickCol = new Vector3f(thisTickRand.nextFloat(), thisTickRand.nextFloat(), thisTickRand.nextFloat()).mul(thisTickRand.nextFloat());
            Vector3f nextTickCol = new Vector3f(nextTickRand.nextFloat(), nextTickRand.nextFloat(), nextTickRand.nextFloat()).mul(nextTickRand.nextFloat());
            Vector3f col = thisTickCol.lerp(nextTickCol, lerp);

            colors.set(col);
        }

        if (data.darkWorld) {
            colors.set(0.0f, 0.0f, 0.0f);
        }
        //super.adjustLightmapColors(level, partialTicks, skyDarken, blockLightRedFlicker, skyLight, pixelX, pixelY, colors);
    }
}
