package diamondwalker.sscary.randomevent.uncommon;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.handler.internal.PlayerFallHandler;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public class DropFromSkyEvent {
    public static boolean execute(MinecraftServer server) {
        RandomSource random = server.overworld().getRandom();

        boolean executed = false;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.isAlive() && !player.getAbilities().flying && (player.getY() >= player.level().getSeaLevel() || Config.ULTRA_SCARY_MODE.get())) {
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
}
