package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;

public abstract class QueryYesNoState extends ConsoleState {
    protected QueryYesNoState(ConsoleScreen console) {
        super(console);
    }

    @Override
    protected final void handleUserInput(String input) {
        if (input.equalsIgnoreCase("yes")) {
            handleYes();
        } else if (input.equalsIgnoreCase("no")) {
            handleNo();
        }
    }

    protected abstract void handleYes();

    protected abstract void handleNo();
}
