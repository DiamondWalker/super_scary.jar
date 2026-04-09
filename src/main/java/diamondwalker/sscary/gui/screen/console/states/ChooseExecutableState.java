package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.network.chat.Component;

public class ChooseExecutableState extends ConsoleState {
    private boolean scanFinished = false;
    private boolean inputAllowed = false;

    private String failedToFindExecutable;

    protected ChooseExecutableState(ConsoleScreen console) {
        super(console);
    }

    @Override
    protected void update() {
        if (scanFinished) {
            if (ticks == 11) {
                console.addLine(Component.literal("Could not find executable '" + failedToFindExecutable + "'. Please select a valid executable."));
                inputAllowed = true;
            }
        } else {
            switch (ticks) {
                case 0 -> {
                    console.addLine(Component.empty());
                    break;
                }
                case 35 -> {
                    console.addLine(Component.literal("Scanning for executables..."));
                    break;
                }
                case 75 -> {
                    console.addLine(Component.empty());
                    console.addLine(Component.literal("1 executable found:"));
                    console.addLine(Component.literal("minecraft.jar"));
                    console.addLine(Component.empty());
                    console.addLine(Component.literal("Please type the name of the executable you would like to launch."));
                    scanFinished = true;
                    inputAllowed = true;
                    break;
                }
            }
        }
    }

    @Override
    public boolean acceptingUserInput() {
        return inputAllowed;
    }

    @Override
    protected void handleUserInput(String input) {
        if (input.equals("minecraft.jar")) {
            console.setState(new DisclaimerState(console));
        } else {
            failedToFindExecutable = input;
            ticks = 0;
            inputAllowed = false;
        }
    }
}
