package diamondwalker.sscary.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class ConsoleScreen extends Screen {
    private final TitleScreen menu;

    private List<Component> messages = new ArrayList<>();
    StringBuilder typing = new StringBuilder();

    private final int topMargin;
    private final int bottomMargin;
    private final int leftMargin;
    private final int rightMargin;
    private final float scale;

    int indicatorTicks = 0;
    int timer = 0;
    boolean acceptingInput = false;

    public ConsoleScreen(TitleScreen titleScreen) {
        super(Component.literal(""));
        this.menu = titleScreen;

        topMargin = bottomMargin = 10;
        leftMargin = rightMargin = 10;
        scale = 0.75f;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, FastColor.ARGB32.color(255, 0, 0, 0));

        int typingAreaWidth = (width - rightMargin) - leftMargin;
        int typingAreaHeight = (height - bottomMargin) - topMargin;
        typingAreaWidth = Mth.floor(typingAreaWidth / scale);
        typingAreaHeight = Mth.floor(typingAreaHeight / scale);
        int lineSpacing = (int)(1.2 * font.lineHeight);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(leftMargin, topMargin, 0);
        guiGraphics.pose().scale(scale, scale, scale);

        List<String> linesToDraw = new ArrayList<>(messages.stream().map(Component::getString).toList());
        String input = typing.toString() + (indicatorTicks % 14 < 7 ?'_' : ' ');
        linesToDraw.add(input);

        List<String> wrappedLines = new ArrayList<>();

        for (String string : linesToDraw) {
            while (!string.isEmpty()) {
                if (font.width(string) > typingAreaWidth) {
                    String prevString = "";

                    for (int i = 0; i <= string.length(); i++) {
                        String currString = string.substring(0, i);
                        if (font.width(currString) <= typingAreaWidth) {
                            prevString = currString;
                        } else {
                            wrappedLines.add(prevString);
                            string = string.substring(prevString.length());
                            break;
                        }
                    }
                } else {
                    wrappedLines.add(string);
                    break;
                }
            }
        }

        int maxLines = Math.round((float)typingAreaHeight / lineSpacing);

        int y = 0;
        for (int i = Math.max(0, wrappedLines.size() - maxLines); i < wrappedLines.size(); i++) {
            guiGraphics.drawString(Minecraft.getInstance().font, wrappedLines.get(i), 0, y, FastColor.ARGB32.color(255, 255, 255, 255), false);
            y += lineSpacing;
        }

        guiGraphics.pose().popPose();
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (!acceptingInput) return false;

        if (key == InputConstants.KEY_BACKSPACE) {
            if (!typing.isEmpty()) typing.deleteCharAt(typing.length() - 1);
            return true;
        } else if (key == InputConstants.KEY_RETURN) {
            String msg = typing.toString();
            messages.add(Component.literal(msg));
            handleMsgEntered(msg);
            typing = new StringBuilder();
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        indicatorTicks++;
        timer++;
        acceptingInput = true;
    }

    private void handleMsgEntered(String msg) {

    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!acceptingInput) return false;
        typing.append(codePoint);
        return true;
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
