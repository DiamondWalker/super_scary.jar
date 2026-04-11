package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.network.chat.Component;

public class DisclaimerState extends QueryYesNoState {
    private boolean prompt = false;

    protected DisclaimerState(ConsoleScreen console) {
        super(console);
    }

    @Override
    protected void update() {
        switch (ticks) {
            case 28 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("Preparing to launch executable 'minecraft.jar'..."));
                break;
            }
            case 63 -> {
                console.addLine(Component.literal("Running code scan..."));
                break;
            }
            case 110 -> {
                console.addLine(Component.literal("§41684 anomalous code traces detected. Extremely high probability of paranormal activity."));
                console.addLine(Component.literal("Remember: this virtual machine will protect you from attacks on your system or physical reality, but program files related to 'minecraft.jar' (e.g. game save files) will still be vulnerable to corruption or deletion."));
                console.addLine(Component.empty());
                console.addLine(Component.literal("Do you understand the risks? (yes/no)"));
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
        console.setState(new LaunchingState(console));
    }

    @Override
    protected void handleNo() {
        console.setState(new StupidIdiotState(console));
    }
}
