package diamondwalker.sscary.util;

import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;

public class WindowUtil {
    private static final Window WINDOW = Minecraft.getInstance().getWindow();

    public static void moveWindow(int x, int y) {
        GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX() + x, WINDOW.getY() + y);
    }

    private static void clampWindowToScreenSize() {
        VideoMode mode = WINDOW.findBestMonitor().getCurrentMode();
        int width = mode.getWidth();
        int height = mode.getHeight();

        if (WINDOW.getX() < 0) GLFW.glfwSetWindowPos(WINDOW.getWindow(), 0, WINDOW.getY());
        if (WINDOW.getY() < 0) GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX(), 0);

        if (WINDOW.getX() + WINDOW.getWidth() >= width) {
            GLFW.glfwSetWindowPos(WINDOW.getWindow(), width - WINDOW.getWidth(), WINDOW.getY());
        }
        if (WINDOW.getY() + WINDOW.getHeight() >= height) {
            GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX(), height - WINDOW.getHeight());
        }

        throw new NotImplementedException();
    }
}
