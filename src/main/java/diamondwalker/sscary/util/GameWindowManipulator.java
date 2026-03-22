package diamondwalker.sscary.util;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;

public class GameWindowManipulator {
    private static final Window WINDOW = Minecraft.getInstance().getWindow();

    public static void moveWindow(int x, int y) {
        GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX() + x, WINDOW.getY() + y);
        clampWindowToScreenSize();
    }

    private static void clampWindowToScreenSize() {
        throw new NotImplementedException();
    }
}
