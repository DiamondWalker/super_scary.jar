package diamondwalker.sscary.gui.screen.console.states;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.gui.screen.console.ConsoleScreen;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.fml.VersionChecker;

public class UpdateCheckState extends QueryYesNoState {
    private boolean foundUpdate = false;
    private String updateUrl = null;

    protected UpdateCheckState(ConsoleScreen console) {
        super(console);
    }

    @Override
    protected void update() {
        VersionChecker.CheckResult version = VersionChecker.getResult(ModList.get().getModContainerById(SScary.MODID).get().getModInfo());

        if (ticks > 40 && !foundUpdate && version.status() != VersionChecker.Status.PENDING) {
            if (version.status() == VersionChecker.Status.OUTDATED) {
                foundUpdate = true;
                console.addLine(Component.empty());
                console.addLine(Component.literal("A software update is available."));
                console.addLine(Component.empty());
                console.addLine(Component.literal("Would you like to install it now? (yes/no)"));

                updateUrl = version.url();
            } else {
                console.setState(new ChooseExecutableState(console));
            }
        }
    }

    @Override
    public boolean acceptingUserInput() {
        return foundUpdate;
    }

    @Override
    protected void handleYes() {
        if (updateUrl == null) throw new IllegalStateException("It should not be possible for the update url to be null here");
        Util.getPlatform().openUri(updateUrl);
    }

    @Override
    protected void handleNo() {
        console.setState(new ChooseExecutableState(console));
    }
}
