package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.network.chat.Component;

public class ScanningExecutablesState extends ConsoleState {
    protected ScanningExecutablesState(ConsoleScreen console) {
        super(console);
    }

    @Override
    protected void update() {
        switch (ticks) {
            case 35 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("Scanning for executables..."));
                break;
            }
            case 75 -> {
                console.setState(new ChooseExecutableState(console));
            }
        }
    }
}
