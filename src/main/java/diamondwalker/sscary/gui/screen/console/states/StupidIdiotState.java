package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class StupidIdiotState extends ConsoleState {
    protected StupidIdiotState(ConsoleScreen console) {
        super(console);
    }

    @Override
    protected void update() {
        switch (ticks) {
            case 40 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("WARNING: Phantom OS has detected that the user is suffering a severe aneurysm and is therefore unable to read a simple disclaimer message."));
                break;
            }
            case 114 -> {
                console.addLine(Component.literal("It is strongly advised you contact emergency services immediately before you become braindead."));
                break;
            }
            case 170 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("(more than you already were)"));
                break;
            }
            case 183 -> {
                console.replaceLastLineWith(Component.literal("The program will now be terminated."));
                break;
            }
            case 225 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("Goodbye."));
            }
            case 275 -> {
                Minecraft.getInstance().stop();
                break;
            }
        }
    }

    @Override
    protected boolean fastTickAllowed() {
        return false;
    }
}
