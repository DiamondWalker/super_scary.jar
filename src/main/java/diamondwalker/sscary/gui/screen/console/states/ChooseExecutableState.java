package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.network.chat.Component;

public class ChooseExecutableState extends ConsoleState {
    private boolean failedToFind = false;

    private String failedToFindExecutable;

    protected ChooseExecutableState(ConsoleScreen console) {
        super(console);
        console.addLine(Component.empty());
        console.addLine(Component.literal("1 executable found:"));
        console.addLine(Component.literal("minecraft.jar"));
        console.addLine(Component.empty());
        console.addLine(Component.literal("Please type the name of the executable you would like to launch."));
    }

    @Override
    protected void update() {
        if (failedToFind && ticks == 11) {
            console.addLine(Component.empty());
            console.addLine(Component.literal("Could not find executable '" + failedToFindExecutable + "'. Please select a valid executable."));
            failedToFind = false;
        }
    }

    @Override
    public boolean acceptingUserInput() {
        return !failedToFind;
    }

    @Override
    protected void handleUserInput(String input) {
        if (input.equals("minecraft.jar")) {
            console.setState(new DisclaimerState(console));
        } else if (input.equals("super_scary.jar")) {
            console.setState(new UltraScaryState(console));
        } else {
            failedToFindExecutable = input;
            ticks = 0;
            failedToFind = true;
        }
    }
}
