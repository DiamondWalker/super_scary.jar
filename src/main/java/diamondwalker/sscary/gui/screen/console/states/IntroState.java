package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.network.chat.Component;

public class IntroState extends ConsoleState {
    public IntroState(ConsoleScreen console) {
        super(console);
    }

    @Override
    public void update() {
        switch (ticks) {
            case 100 -> {
                console.addLine(Component.literal("Welcome to Phantom OS."));
                break;
            }
            case 150 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("Logging in..."));
                break;
            }
            case 220 -> {
                console.setState(new QueryUsernameState(console));
                break;
            }
        }
    }
}
