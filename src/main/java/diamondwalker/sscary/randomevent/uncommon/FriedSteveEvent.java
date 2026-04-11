package diamondwalker.sscary.randomevent.uncommon;

import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.script.randomevent.FriedSteveScript;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class FriedSteveEvent extends RandomEvent {
    @Override
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        return WorldData.get(server).newScripts.startScript(new FriedSteveScript(server));
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.UNCOMMON;
    }
}
