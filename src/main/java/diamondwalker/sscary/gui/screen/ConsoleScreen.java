package diamondwalker.sscary.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import diamondwalker.sscary.data.PermanentSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.*;

public class ConsoleScreen extends Screen {
    private final TitleScreen menu;

    private List<Component> messages = new ArrayList<>();
    StringBuilder typing = new StringBuilder();

    private final int topMargin;
    private final int bottomMargin;
    private final int leftMargin;
    private final int rightMargin;
    private final float scale;

    private int indicatorTicks = 0;
    private State state = State.BLOCKED;

    private LinkedList<QueuedEvent> events = new LinkedList<>();

    public ConsoleScreen(TitleScreen titleScreen) {
        super(Component.literal(""));
        this.menu = titleScreen;

        topMargin = bottomMargin = 10;
        leftMargin = rightMargin = 10;
        scale = 0.75f;

        queueEvent(100, () -> {
            messages.add(Component.literal("Welcome to Phantom OS."));
        });

        queueEvent(150, () -> {
            messages.add(Component.empty());
            messages.add(Component.literal("Logging in..."));
        });

        queueEvent(220, () -> {
            queryUsername();
        });
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
        String input = typing.toString() + (state != State.BLOCKED && indicatorTicks % 14 < 7 ? '_' : ' ');
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
        if (state == State.BLOCKED) return false;

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

        Iterator<QueuedEvent> eventIterator = events.iterator();
        while (eventIterator.hasNext()) {
            QueuedEvent next = eventIterator.next();
            if (next.time-- <= 0) {
                eventIterator.remove();
                next.action.run();
            }
        }
    }

    private void handleMsgEntered(String msg) {
        switch (state) {
            case CHANGE_USERNAME -> {
                if (msg.equalsIgnoreCase("yes")) {
                    state = State.NEW_USERNAME;
                    messages.add(Component.empty());
                    messages.add(Component.literal("Type your new username:"));
                } else if (msg.equalsIgnoreCase("no")) {
                    scanExecutables();
                }
                break;
            }
            case NEW_USERNAME -> {
                PermanentSaveData data = PermanentSaveData.getOrCreateInstance();
                data.setUsername(msg);
                data.saveChanges();
                queryUsername();
                break;
            }
            case CHOOSE_EXECUTABLE -> {
                if (Objects.equals(msg, "minecraft.jar")) {
                    state = State.BLOCKED;
                    messages.add(Component.empty());
                    queueEvent(28, () -> messages.add(Component.literal("Preparing to launch executable 'minecraft.jar'...")));
                    queueEvent(63, () -> messages.add(Component.literal("Running code scan...")));
                    queueEvent(110, () -> {
                        messages.add(Component.literal("§41684 anomalous code traces detected. Extremely high probability of paranormal activity."));
                        messages.add(Component.literal("Remember: this virtual machine will protect you from attacks on your system or physical reality, but program files related to 'minecraft.jar' (e.g. game save files) will still be vulnerable to corruption or deletion."));
                        messages.add(Component.empty());
                        messages.add(Component.literal("Do you understand the risks? (yes/no)"));
                        state = State.DISCLAIMER;
                    });
                } else {
                    state = State.BLOCKED;
                    queueEvent(11, () -> {
                        messages.add(Component.literal("Could not find executable '" + msg + "'. Please select a valid executable."));
                        state = State.CHOOSE_EXECUTABLE;
                    });
                }
                break;
            }
            case DISCLAIMER -> {
                if (msg.equalsIgnoreCase("yes")) {
                    state = State.BLOCKED;
                    queueEvent(33, () -> {
                        messages.add(Component.empty());
                        messages.add(Component.literal("Launching executable 'minecraft.jar'..."));
                    });
                    queueEvent(110, () -> messages.clear());
                    queueEvent(160, () -> Minecraft.getInstance().setScreen(menu));
                } else if (msg.equalsIgnoreCase("no")) {
                    state = State.BLOCKED;
                    queueEvent(40, () -> {
                        messages.add(Component.empty());
                        messages.add(Component.literal("WARNING: Phantom OS has detected that the user is suffering a severe aneurysm and is therefore unable to read a simple disclaimer message."));
                    });
                    queueEvent(114, () -> messages.add(Component.literal("It is strongly advised you contact emergency services immediately before you become braindead.")));
                    queueEvent(170, () -> {
                        messages.add(Component.empty());
                        messages.add(Component.literal("(more than you already were)"));
                    });
                    queueEvent(183, () -> messages.set(messages.size() - 1, Component.literal("The program will now be terminated.")));
                    queueEvent(225, () -> {
                        messages.add(Component.empty());
                        messages.add(Component.literal("Goodbye."));
                    });
                    queueEvent(275, () -> Minecraft.getInstance().stop());
                }
                break;
            }
        }
    }

    private void queryUsername() {
        messages.add(Component.literal("Logged in as: " + PermanentSaveData.getOrCreateInstance().getUsername()));
        messages.add(Component.empty());
        messages.add(Component.literal("Would you like to change your username? (yes/no)"));
        state = State.CHANGE_USERNAME;
    }

    private void scanExecutables() {
        state = State.BLOCKED;
        messages.add(Component.empty());
        queueEvent(35, () -> messages.add(Component.literal("Scanning for executables...")));
        queueEvent(75, () -> {
            messages.add(Component.empty());
            messages.add(Component.literal("1 executable found:"));
            messages.add(Component.literal("minecraft.jar"));
            messages.add(Component.empty());
            messages.add(Component.literal("Please type the name of the executable you would like to launch."));
            state = State.CHOOSE_EXECUTABLE;
        });
    }

    private void queueEvent(int ticks, Runnable event) {
        events.add(new QueuedEvent(ticks, event));
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (state == State.BLOCKED) return false;
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

    private class QueuedEvent {
        private int time;
        private final Runnable action;

        protected QueuedEvent(int time, Runnable action) {
            this.time = time;
            this.action = action;
        }
    }

    private enum State {
        BLOCKED,
        CHANGE_USERNAME,
        NEW_USERNAME,
        CHOOSE_EXECUTABLE,
        DISCLAIMER
    }
}
