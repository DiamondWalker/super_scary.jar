package diamondwalker.sscary.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import diamondwalker.sscary.TWAIS;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.StaticData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public class StaticOverlay implements LayeredDraw.Layer {
    private static final ResourceLocation STATIC_TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "textures/gui/static.png");

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        float xScale = 1.0f;
        float yScale = 1.0f;
        float alpha = 0.03f;
        int interval = 200_000_000;

        StaticData data = ClientData.get().staticData;
        if (data != null) {
            xScale = data.xScale();
            yScale = data.yScale();
            alpha = data.alpha();
            interval = data.interval();
        }

        long seed = System.nanoTime() / interval;
        Random rand = new Random(seed);

        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) guiGraphics.guiWidth() / 2, (float) guiGraphics.guiHeight() / 2, 0.0f);
        guiGraphics.pose().scale(rand.nextBoolean() ? xScale : -xScale, rand.nextBoolean() ? yScale : -yScale, 1.0f);
        guiGraphics.pose().translate((float) -guiGraphics.guiWidth() / 2, (float) -guiGraphics.guiHeight() / 2, 0.0f);
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, alpha);
        guiGraphics.blit(STATIC_TEXTURE_LOCATION, 0, 0, rand.nextInt(256), rand.nextInt(256), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        guiGraphics.pose().popPose();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
