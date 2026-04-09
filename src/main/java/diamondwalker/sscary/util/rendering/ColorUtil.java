package diamondwalker.sscary.util.rendering;

import net.minecraft.util.FastColor;

public class ColorUtil {
    public static final int WHITE = color(255, 255, 255, 255);
    public static final int BLACK = color(0, 0, 0, 255);

    public static int color(int red, int green, int blue, int alpha) {
        if (alpha < 4) alpha = 4;
        return FastColor.ARGB32.color(alpha, red, green, blue);
    }

    public static int color(float red, float green, float blue, float alpha) {
        return color(
                Math.round(red * 255),
                Math.round(green * 255),
                Math.round(blue * 255),
                Math.round(alpha * 255)
        );
    }
}
