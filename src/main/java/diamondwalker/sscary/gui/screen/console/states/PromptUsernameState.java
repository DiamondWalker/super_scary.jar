package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.data.PermanentSaveData;
import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.network.chat.Component;

public class PromptUsernameState extends ConsoleState {
    protected PromptUsernameState(ConsoleScreen console) {
        super(console);

        console.addLine(Component.empty());
        console.addLine(Component.literal("Type your new username:"));
    }

    @Override
    public boolean acceptingUserInput() {
        return true;
    }

    @Override
    protected void handleUserInput(String input) {
        PermanentSaveData data = PermanentSaveData.getOrCreateInstance();
        data.setUsername(input);
        data.saveChanges();
        console.setState(new QueryUsernameState(console));
    }
}
