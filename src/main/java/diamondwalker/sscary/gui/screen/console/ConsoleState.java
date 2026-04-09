package diamondwalker.sscary.gui.screen.console;

import diamondwalker.sscary.Config;
import net.minecraft.network.chat.Component;

import java.awt.*;

public abstract class ConsoleState {
    protected int ticks = 0;
    protected final ConsoleScreen console;

    protected ConsoleState(ConsoleScreen console) {
        this.console = console;
    }

    public boolean acceptingUserInput() {
        return false;
    }

    protected void handleUserInput(String input) {}

    final void tick() {
        update();
        ticks++;
    }

    protected void update() {}

    protected boolean fastTickAllowed() {
        return true;
    }
}
