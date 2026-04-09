package diamondwalker.sscary.randomevent.uncommon;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.data.CommonData;
import diamondwalker.sscary.handler.internal.PlayerFallHandler;
import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public class DropFromSkyEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        boolean executed = false;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.isAlive() && !player.getAbilities().flying && (player.getY() >= player.level().getSeaLevel() || CommonData.ultraScaryMode)) {
                new ScriptBuilder(server)
                        .action((serv) -> player.teleportTo(player.getX(), Math.max(500, player.level().getMaxBuildHeight()), player.getZ()))
                        .rest(1)
                        .action((serv) -> PlayerFallHandler.disableFall(player))
                        .startScript();
                executed = true;
            }
        }

        return executed;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.UNCOMMON;
    }
}
