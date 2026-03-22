package diamondwalker.sscary.util;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;

public class GameWindowManipulator {
    private final Window window = Minecraft.getInstance().getWindow();

    public void moveWindow(int x, int y) {
        GLFW.glfwSetWindowPos(window.getWindow(), window.getX() + x, window.getY() + y);
        clampWindowToScreenSize();
    }

    private void clampWindowToScreenSize() {
        throw new NotImplementedException();
    }
}
