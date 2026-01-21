package diamondwalker.twais.gui.screen;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.data.client.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class FakePauseScreen extends PauseScreen {
    private int ticks = 100;
    private StringWidget titleWidget; // we use this to override the title message. If this is null it means we failed to access the title and will imitate regular pause behavior
    boolean fullyDeleted = false;
    boolean startedTypingName = false;
    boolean close = false;
    private Queue<Character> messageP1 = new LinkedList<>();
    private Queue<Character> messageP2 = new LinkedList<>();

    public FakePauseScreen(boolean showPauseMenu) {
        super(showPauseMenu);

        for (char character : Component.literal("Stop hiding behind that screen, ").getString().toCharArray()) {
            messageP1.add(character);
        }

        for (char character : Component.literal(Minecraft.getInstance().player.getName().getString() + ".").getString().toCharArray()) {
            messageP2.add(character);
        }
    }

    @Override
    protected void init() {
        super.init();

        for (Renderable renderable : renderables) {
            if (renderable instanceof StringWidget string && string.getMessage() == title) {
                titleWidget = string;
            }
        }

        if (titleWidget != null) {
            for (Renderable renderable : renderables) {
                if (renderable instanceof Button button) {
                    button.onPress = (but) -> {};
                }
            }
        } else {
            TWAIS.LOGGER.warn("Visage's custom pause screen could not find the screen title widget to implement custom behavior. Vanilla behavior will be mimicked!");
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (titleWidget != null) {
            if (ticks <= 0) {
                if (close) {
                    this.minecraft.setScreen(null);
                    this.minecraft.mouseHandler.grabMouse();
                    ClientData.get().blockPause();
                }
                if (!fullyDeleted) {
                    if (!removeCharacter()) {
                        fullyDeleted = true;
                        ticks = 30;
                    } else {
                        ticks = 6;
                    }
                } else {
                    if (!messageP1.isEmpty()) {
                        addCharacter(messageP1.poll());
                        ticks = 2;
                    } else {
                        if (!startedTypingName) {
                            ticks = 10;
                            startedTypingName = true;
                        } else {
                            if (!messageP2.isEmpty()) {
                                addCharacter(messageP2.poll());
                                ticks = 2;
                            } else {
                                ticks = 20;
                                close = true;
                            }
                        }
                    }
                }
            } else {
                ticks--;
            }
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private boolean removeCharacter() {
        String title = titleWidget.getMessage().getString();
        if (title.isEmpty()) return false;
        titleWidget.setMessage(Component.literal(title.substring(0, title.length() - 1)));
        return true;
    }

    private void addCharacter(char c) {
        titleWidget.setMessage(Component.literal(titleWidget.getMessage().getString() + c));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (titleWidget != null) {
            boolean result = super.mouseClicked(mouseX, mouseY, button);
            this.setFocused(null);
            return result;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
