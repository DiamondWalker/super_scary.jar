package diamondwalker.twais.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CommonButtons;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.Music;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;

public class NoticeScreen extends Screen {
    private final TitleScreen menu;

    public NoticeScreen(TitleScreen titleScreen) {
        super(Component.literal(""));
        this.menu = titleScreen;
    }

    @Override
    protected void init() {
        super.init();

        this.addRenderableWidget(
                Button.builder(Component.literal("OK"), p_280832_ -> this.minecraft.setScreen(menu))
                        .bounds(this.width / 2 - 50, height / 2 + minecraft.font.lineHeight * 9, 100, 20)
                        .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, FastColor.ARGB32.color(255, 0, 0, 0));

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(width / 2, height / 2, 0);
        guiGraphics.pose().scale(0.8f, 0.8f, 0.8f);

        Component warning = Component.literal("WARNING!").withStyle(ChatFormatting.UNDERLINE).withColor(FastColor.ARGB32.color(255, 0, 0));

        Component[] lines = new Component[] {
                Component.literal("This mod can have several effects on your game and system. These include:"),
                Component.empty(),
                Component.literal("-destroying your builds"),
                Component.literal("-banning you from your world"),
                Component.literal("-crashing your game"),
                Component.literal("-creating system files"),
                Component.literal("-revealing your IP Address"),
                Component.literal("-shutting down your PC"),
                Component.literal("-formatting your hard drive"),
                Component.literal("-using your PC for bitcoin mining"),
                Component.literal("-calling a drone strike on your location"),
                Component.empty(),
                Component.literal("This mod is not a virus; these features have been added to augment your horror experience.").withColor(FastColor.ARGB32.color(255, 255, 0))
        };

        Font font = Minecraft.getInstance().font;

        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-font.width(warning), 0, 0);
        guiGraphics.pose().translate(0, (double) (-(lines.length + 1) / 2 * font.lineHeight), 0);
        guiGraphics.pose().scale(2.0f, 2.0f, 2.0f);
        guiGraphics.drawString(font, warning, 0, -font.lineHeight, FastColor.ARGB32.color(255, 255, 255, 255));
        guiGraphics.pose().popPose();

        for (int i = 0; i < lines.length; i++) {
            Component line = lines[i];
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(-font.width(line) / 2, 0, 0);
            guiGraphics.pose().translate(0, (double) (-(lines.length + 1) / 2 * font.lineHeight), 0);
            guiGraphics.pose().translate(0, 12 * i, 0);
            guiGraphics.drawString(font, line, 0, font.lineHeight / 2, FastColor.ARGB32.color(255, 255, 255, 255));
            guiGraphics.pose().popPose();
        }

        guiGraphics.pose().popPose();

        for (Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
