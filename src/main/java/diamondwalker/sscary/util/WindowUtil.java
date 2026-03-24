package diamondwalker.sscary.util;

import com.mojang.blaze3d.platform.IconSet;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import diamondwalker.sscary.SScary;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
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

    public static void setWindowPos(int x, int y) {
        GLFW.glfwSetWindowPos(WINDOW.getWindow(), x, y);
    }

    public static void moveWindow(int x, int y) {
        setWindowPos(WINDOW.getX() + x, WINDOW.getY() + y);
    }

    public static void setWindowSize(int width, int height) {
        WINDOW.setWindowed(width, height);
    }

    public static void setIcon(ResourceLocation location) {
        if (GLFW.glfwGetPlatform() == 393218) {
            SScary.LOGGER.warn("Window icon change is not currently supported on MacOS"); // TODO: Mac support someday?
        }

        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(location);
        if (resource.isPresent()) {
            try (NativeImage image = NativeImage.read(resource.get().open())) {
                ByteBuffer bytebuffer = null;
                try (MemoryStack memorystack = MemoryStack.stackPush()) {
                    GLFWImage.Buffer buffer = GLFWImage.malloc(1, memorystack);

                    bytebuffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
                    bytebuffer.asIntBuffer().put(image.getPixelsRGBA());
                    buffer.position(0);
                    buffer.width(image.getWidth());
                    buffer.height(image.getHeight());
                    buffer.pixels(bytebuffer);

                    GLFW.glfwSetWindowIcon(WINDOW.getWindow(), buffer.position(0));
                } finally {
                    if (bytebuffer != null) MemoryUtil.memFree(bytebuffer);
                }
            } catch (IOException exception) {
                SScary.LOGGER.error("Couldn't set window icon", exception);
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

    public static int getWindowWidth() {
        return WINDOW.getWidth();
    }

    public static int getWindowHeight() {
        return WINDOW.getHeight();
    }

    public static Dimension getDesktopDimensions() {
        VideoMode mode = WINDOW.findBestMonitor().getCurrentMode();
        return new Dimension(mode.getWidth(), mode.getHeight());
    }

    public static void clampWindowToScreenSize() {
        Dimension desktop = getDesktopDimensions();

        int header = getWindowHeaderWidth();

        if (WINDOW.getX() < 0) GLFW.glfwSetWindowPos(WINDOW.getWindow(), 0, WINDOW.getY());
        if (WINDOW.getY() - header < 0) GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX(), header);

        if (WINDOW.getX() + WINDOW.getWidth() >= desktop.width) {
            GLFW.glfwSetWindowPos(WINDOW.getWindow(), desktop.width - WINDOW.getWidth(), WINDOW.getY());
        }
        if (WINDOW.getY() + WINDOW.getHeight() >= desktop.height) {
            GLFW.glfwSetWindowPos(WINDOW.getWindow(), WINDOW.getX(), desktop.height - WINDOW.getHeight());
        }
    }

    public static int getWindowHeaderWidth() {
        int[] top = new int[1];
        GLFW.glfwGetWindowFrameSize(WINDOW.getWindow(), new int[1], top, new int[1], new int[1]); // we don't care about the others so we just use dummy arrays
        return top[0];
    }
}
