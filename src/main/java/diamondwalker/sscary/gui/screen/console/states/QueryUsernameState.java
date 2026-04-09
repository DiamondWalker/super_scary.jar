package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.data.PermanentSaveData;
import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.network.chat.Component;

public class QueryUsernameState extends QueryYesNoState {
    protected QueryUsernameState(ConsoleScreen console) {
        super(console);

        console.addLine(Component.literal("Logged in as: " + PermanentSaveData.getOrCreateInstance().getUsername()));
        console.addLine(Component.empty());
        console.addLine(Component.literal("Would you like to change your username? (yes/no)"));
    }

    @Override
    public boolean acceptingUserInput() {
        return true;
    }

    @Override
    protected void handleYes() {
        console.setState(new PromptUsernameState(console));
    }

    @Override
    protected void handleNo() {
        console.setState(new UpdateCheckState(console));
    }
}
