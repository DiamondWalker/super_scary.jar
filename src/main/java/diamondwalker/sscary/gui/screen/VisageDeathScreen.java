package diamondwalker.sscary.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.sound.StaticSoundInstance;
import diamondwalker.sscary.sound.VisageDeathStaticSoundInstance;
import diamondwalker.sscary.util.WindowUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import org.joml.Vector2i;

import java.awt.*;

public class VisageDeathScreen extends Screen {
    private static final ResourceLocation STATIC_TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/gui/static.png");

    private Vector2i screenPos = new Vector2i();
    private Dimension desktopSize;
    private final RandomSource random = RandomSource.create();

    private int time = 0;

    public VisageDeathScreen() {
        super(Component.literal(""));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    public boolean playStaticSound() {
        return time % 25 < 8;
    }

    @Override
    protected void init() {
        WindowUtil.setTitle("");
        WindowUtil.setIcon(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "textures/blank.png"));
        randomize();
        Minecraft.getInstance().getSoundManager().queueTickingSound(new VisageDeathStaticSoundInstance(this));
    }

    @Override
    public void tick() {
        if (time++ % 25 == 0) randomize();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int offX = random.nextInt(desktopSize.width + 1) - desktopSize.width / 2;
        int offY = random.nextInt(desktopSize.height + 1) - desktopSize.height / 2;
        WindowUtil.setWindowPos(screenPos.x + offX / 80, screenPos.y + offY / 80);

        guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), FastColor.ARGB32.color(0, 0, 0));

        if (playStaticSound()) {
            RenderSystem.enableBlend();
            RenderSystem.disableCull();

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float) guiGraphics.guiWidth() / 2, (float) guiGraphics.guiHeight() / 2, 0.0f);
            guiGraphics.pose().scale(random.nextBoolean() ? 1.0f : -1.0f, random.nextBoolean() ? 1.0f : -1.0f, 1.0f);
            guiGraphics.pose().translate((float) -guiGraphics.guiWidth() / 2, (float) -guiGraphics.guiHeight() / 2, 0.0f);
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            guiGraphics.blit(STATIC_TEXTURE_LOCATION, 0, 0, random.nextInt(256), random.nextInt(256), guiGraphics.guiWidth(), guiGraphics.guiHeight());
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            guiGraphics.pose().popPose();

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        }
    }

    private void randomize() {
        desktopSize = WindowUtil.getDesktopDimensions();

        WindowUtil.setWindowSize(desktopSize.width / 2, desktopSize.height / 2);

        int minX = 0;
        int maxX = desktopSize.width - WindowUtil.getWindowWidth();

        int minY = WindowUtil.getWindowHeaderWidth();
        int maxY = desktopSize.height - WindowUtil.getWindowHeight();

        screenPos.set(random.nextInt(minX, maxX), random.nextInt(minY, maxY));
    }
}
