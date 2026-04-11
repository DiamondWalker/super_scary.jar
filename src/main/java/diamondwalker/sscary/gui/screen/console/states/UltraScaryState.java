package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.data.CommonData;
import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.network.chat.Component;

public class UltraScaryState extends QueryYesNoState {
    private boolean prompt = false;

    protected UltraScaryState(ConsoleScreen console) {
        super(console);
    }

    @Override
    protected void update() {
        switch (ticks) {
            case 31 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("Whoaoa thats a really scary name you typed"));
                break;
            }
            case 64 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("Would you like to enable ultra_scary mode? (yes/no)"));
                prompt = true;
                break;
            }
        }
    }

    @Override
    public boolean acceptingUserInput() {
        return prompt;
    }

    @Override
    protected void handleYes() {
        CommonData.ultraScaryMode = true;
        console.setState(new LaunchingState(console));
    }

    @Override
    protected void handleNo() {
        console.setState(new ChooseExecutableState(console));
    }
}
