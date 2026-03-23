package diamondwalker.sscary.util;

import com.mojang.blaze3d.platform.IconSet;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import diamondwalker.sscary.SScary;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class WindowUtil {
    private static final Window WINDOW = Minecraft.getInstance().getWindow();

    public static void setTitle(String title) {
        WINDOW.setTitle(title);
    }

    public static void resetTitle() {
        Minecraft.getInstance().updateTitle();
    }

    public static void moveWindow(int x, int y) {
        GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX() + x, WINDOW.getY() + y);
    }

    public static void setIcon(Supplier<NativeImage> supplier) {
        if (GLFW.glfwGetPlatform() == 393218) {
            SScary.LOGGER.warn("Window icon change is not currently supported on MacOS"); // TODO: Mac support someday?
        }

        try (NativeImage image = supplier.get()) {
            ByteBuffer bytebuffer = null;
            try (MemoryStack memorystack = MemoryStack.stackPush()) {
                GLFWImage.Buffer buffer = GLFWImage.malloc(1, memorystack);

                bytebuffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
                bytebuffer.asIntBuffer().put(image.getPixelsRGBA());
                buffer.position(0);
                buffer.width(image.getWidth());
                buffer.height(image.getHeight());

                GLFW.glfwSetWindowIcon(WINDOW.getWindow(), buffer.position(0));
            } finally {
                if (bytebuffer != null) MemoryUtil.memFree(bytebuffer);
            }
        }
    }

    public static void resetIcon() {
        try {
            WINDOW.setIcon(Minecraft.getInstance().getVanillaPackResources(), SharedConstants.getCurrentVersion().isStable() ? IconSet.RELEASE : IconSet.SNAPSHOT);
        } catch (IOException ioexception) {
            SScary.LOGGER.error("Couldn't reset window icon", ioexception);
        }
    }

    public static void clampWindowToScreenSize() {
        VideoMode mode = WINDOW.findBestMonitor().getCurrentMode();
        int width = mode.getWidth();
        int height = mode.getHeight();

        int[] top = new int[1];
        int[] left = new int[1];
        int[] right = new int[1];
        int[] bottom = new int[1];
        GLFW.glfwGetWindowFrameSize(WINDOW.getWindow(), left, top, right, bottom);

        if (WINDOW.getX() < 0) GLFW.glfwSetWindowPos(WINDOW.getWindow(), 0, WINDOW.getY());
        if (WINDOW.getY() - top[0] < 0) GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX(), top[0]);

        if (WINDOW.getX() + WINDOW.getWidth() >= width) {
            GLFW.glfwSetWindowPos(WINDOW.getWindow(), width - WINDOW.getWidth(), WINDOW.getY());
        }
        if (WINDOW.getY() + WINDOW.getHeight() >= height) {
            GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX(), height - WINDOW.getHeight());
        }
    }
}
