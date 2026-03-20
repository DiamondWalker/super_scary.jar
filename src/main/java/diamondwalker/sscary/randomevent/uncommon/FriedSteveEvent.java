package diamondwalker.sscary.randomevent.uncommon;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.script.FriedSteveScript;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class FriedSteveEvent extends RandomEvent {
    @Override
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        WorldData.get(server).newScripts.startScript(new FriedSteveScript(server));
        return true;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.UNCOMMON;
    }
}
