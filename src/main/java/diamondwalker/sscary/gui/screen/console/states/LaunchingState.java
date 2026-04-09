package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import diamondwalker.sscary.gui.screen.console.ConsoleState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class LaunchingState extends ConsoleState {
    protected LaunchingState(ConsoleScreen console) {
        super(console);
    }

    @Override
    protected void update() {
        switch (ticks) {
            case 33 -> {
                console.addLine(Component.empty());
                console.addLine(Component.literal("Launching executable 'minecraft.jar'..."));
                break;
            }
            case 110 -> {
                console.clearText();
                break;
            }
            case 160 -> {
                console.launchMinecraft();
                break;
            }
        }
    }
}
