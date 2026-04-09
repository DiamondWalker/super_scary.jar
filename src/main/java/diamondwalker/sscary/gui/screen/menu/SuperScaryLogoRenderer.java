package diamondwalker.sscary.gui.screen.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import diamondwalker.sscary.SScary;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.ResourceLocation;

public class SuperScaryLogoRenderer extends LogoRenderer {
    public static final ResourceLocation SUPER_SCARY_LOGO = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "icon.png");
    private final boolean keepLogoThroughFade;

    public SuperScaryLogoRenderer(boolean keepLogoThroughFade) {
        super(keepLogoThroughFade);
        this.keepLogoThroughFade = keepLogoThroughFade;
    }

    @Override
    public void renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency, int height) {
        //height += 17;
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.keepLogoThroughFade ? 1.0F : transparency);
        RenderSystem.enableBlend();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(screenWidth / 2, height, 0);
        guiGraphics.pose().scale(0.9f, 0.9f, 0.9f);
        guiGraphics.pose().translate(-242, 0, 0);
        guiGraphics.blit(SUPER_SCARY_LOGO, 0, 0, 0.0F, 0.0F, 484, 47, 484, 47);
        guiGraphics.pose().popPose();
        int j = screenWidth / 2 - 64;
        int k = height + 44 - 7;
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}
