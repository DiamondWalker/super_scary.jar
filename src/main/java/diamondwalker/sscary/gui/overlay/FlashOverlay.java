package diamondwalker.sscary.gui.overlay;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.ScreenFlashData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.FastColor;

public class FlashOverlay implements LayeredDraw.Layer {
    public static final long DURATION = 300_000_000;
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ScreenFlashData flash = ClientData.get().flash;
        if (flash != null) {
            long timeElapsed = System.nanoTime() - flash.time();
            float f = ((float) timeElapsed / DURATION - 0.5f) * 2; // range from -1 to 1
            float alpha = 1.0f - Math.abs(f);
            if (alpha > 0) {
                int color = FastColor.ARGB32.color(Math.round(alpha * 255), Math.round(flash.red() * 255), Math.round(flash.green() * 255), Math.round(flash.blue() * 255));
                guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), color);
            } else {
                ClientData.get().flash = null; // so we don't have to do all these checks next time
            }
        }
    }
}
