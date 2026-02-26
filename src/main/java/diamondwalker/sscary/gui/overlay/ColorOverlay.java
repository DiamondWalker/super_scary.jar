package diamondwalker.sscary.gui.overlay;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.ColorOverlayData;
import diamondwalker.sscary.data.client.ScreenFlashData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.FastColor;

public class ColorOverlay implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ColorOverlayData overlay = ClientData.get().colorOverlay;
        if (overlay != null) {
            int color = FastColor.ARGB32.color(Math.round(overlay.alpha() * 255), Math.round(overlay.red() * 255), Math.round(overlay.green() * 255), Math.round(overlay.blue() * 255));
            guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), color);
        }
    }
}
