package diamondwalker.sscary.gui.screen.menu;

import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class SuperScarySplashRenderer extends SplashRenderer {
    public SuperScarySplashRenderer() {
        super(getRandomSplash());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int screenWidth, Font font, int color) {
        long time = System.nanoTime();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float)screenWidth / 2.0F, 45.0F, 0.0F);
        guiGraphics.pose().translate(Math.sin((double)time / 1.22e9) * 200, Math.cos((double)time / 0.33e9) * 30, 0);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(20.0F * (float)Math.cos((double)time / 0.18e9)));

        double rotTime = time % 10e9;
        if (rotTime > 8e9) {
            rotTime -= 8e9;
            rotTime /= 2e9;
            rotTime = (((6.0f*rotTime)-15.0f)*rotTime+10.0f)*rotTime*rotTime*rotTime;

            if ((time / (long)10e9) % 2 == 0) {
                rotTime = -rotTime;
            }

            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float)(360.0 * rotTime * 2)));
        }

        //float f = 1.8F - Mth.abs(Mth.sin((float)(Util.getMillis() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
        //f = f * 100.0F / (float)(font.width(this.splash) + 32);
        //guiGraphics.pose().scale(f, f, f);
        guiGraphics.drawCenteredString(font, this.splash, 0, 0, 16776960 | color);
        guiGraphics.pose().popPose();
    }

    private static String getRandomSplash() {
        RandomSource random = RandomSource.create();
        switch (random.nextInt(5)) {
            case 0 -> {
                return "So scary!!!";
            }
            case 1 -> {
                return "WHOA SCARY";
            }
            case 2 -> {
                return "Makes you poop!";
            }
            case 3 -> {
                return "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH";
            }
            default -> {
                return "Leaks all your personal information!";
            }

        }
    }
}
