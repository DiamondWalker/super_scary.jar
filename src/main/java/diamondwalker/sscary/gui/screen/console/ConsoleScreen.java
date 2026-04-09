package diamondwalker.sscary.gui.screen.console;

import com.mojang.blaze3d.platform.InputConstants;
import diamondwalker.sscary.Config;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.PermanentSaveData;
import diamondwalker.sscary.gui.screen.console.states.IntroState;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.neoforged.fml.ModList;
import net.neoforged.fml.VersionChecker;

import java.util.*;
import java.util.function.Function;

public class ConsoleScreen extends Screen {
    private final TitleScreen menu;

    List<Component> messages = new ArrayList<>();
    StringBuilder typing = new StringBuilder();

    private final int topMargin;
    private final int bottomMargin;
    private final int leftMargin;
    private final int rightMargin;
    private final float scale;

    private int indicatorTicks = 0;
    ConsoleState state;

    public ConsoleScreen(TitleScreen titleScreen) {
        super(Component.empty());
        this.menu = titleScreen;

        topMargin = bottomMargin = 10;
        leftMargin = rightMargin = 10;
        scale = 0.75f;

        state = new IntroState(this);
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
        String input = typing.toString() + (state.acceptingUserInput() && indicatorTicks % 14 < 7 ? '_' : ' ');
        linesToDraw.add(input);

        List<String> wrappedLines = new ArrayList<>();

        for (String string : linesToDraw) {
            if (string.isEmpty()) {
                wrappedLines.add(string);
                continue;
            }
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
        if (!state.acceptingUserInput()) return false;

        if (key == InputConstants.KEY_BACKSPACE) {
            if (!typing.isEmpty()) typing.deleteCharAt(typing.length() - 1);
            return true;
        } else if (key == InputConstants.KEY_RETURN) {
            String msg = typing.toString();
            messages.add(Component.literal(msg));
            state.handleUserInput(msg);
            typing = new StringBuilder();
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        indicatorTicks++;
        state.tick();
        if (Config.QUICK_STARTUP.get() && state.fastTickAllowed()) {
            // 5 times faster ticking
            for (int i = 0; i < 4; i++) {
                state.tick();
            }
        }
    }

    public void addLine(Component text) {
        messages.add(text);
    }

    public void clearText() {
        messages.clear();
    }

    public void launchMinecraft() {
        Minecraft.getInstance().setScreen(menu);
    }

    public void replaceLastLineWith(Component text) {
        messages.set(messages.size() - 1, text);
    }

    public void setState(ConsoleState state) {
        this.state = state;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!state.acceptingUserInput()) return false;
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
