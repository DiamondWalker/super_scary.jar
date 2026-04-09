package diamondwalker.sscary.gui.screen;

import diamondwalker.sscary.util.rendering.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class DisclaimerScreen extends Screen {
    private int time;
    private final TitleScreen screen;

    public DisclaimerScreen(TitleScreen titleScreen) {
        super(Component.empty());
        this.screen = titleScreen;
    }

    @Override
    public void tick() {
        if (time++ >= 200) {
            Minecraft.getInstance().setScreen(new ConsoleScreen(screen));
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, FastColor.ARGB32.color(255, 0, 0, 0));

        float f = (partialTick + time) / 200;

        float f1 = Mth.clamp((f - 0.3f) / 0.1f, 0, 1);
        float f2 = Mth.clamp((f - 0.6f) / 0.1f, 0, 1);
        float f3 = 1.0f - Mth.clamp((f - 0.9f) / 0.1f, 0, 1);
        f1 = Math.min(f1, f3);
        f2 = Math.min(f2, f3);

        int y1 = Math.round(0.33f * guiGraphics.guiHeight()) - font.lineHeight / 2;
        int y2 = Math.round(0.66f * guiGraphics.guiHeight()) - font.lineHeight / 2;

        Component line1 = Component.translatable("sscary.screen.disclaimer_0");
        Component line2 = Component.translatable("sscary.screen.disclaimer_1");

        if (f1 > 0) {
            guiGraphics.drawString(
                    font,
                    line1,
                    (guiGraphics.guiWidth() - font.width(line1)) / 2,
                    y1,
                    ColorUtil.color(1.0f, 1.0f, 1.0f, f1)
            );
        }
        if (f2 > 0) {
            guiGraphics.drawString(
                    font,
                    line2,
                    (guiGraphics.guiWidth() - font.width(line2)) / 2,
                    y2,
                    ColorUtil.color(1.0f, 1.0f, 1.0f, f2)
            );
        }
    }
}
