package diamondwalker.twais.randomevent.uncommon;

import diamondwalker.twais.Config;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public class DropFromSkyEvent {
    public static boolean execute(MinecraftServer server) {
        RandomSource random = server.overworld().getRandom();

        boolean executed = false;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.isAlive() && !player.getAbilities().flying) {
                new ScriptBuilder(server)
                        .action((serv) -> player.teleportTo(player.getX(), Math.max(500, player.level().getMaxBuildHeight()), player.getZ()))
                        .rest(1)
                        .action((serv) -> player.fallDistance = -1000)
                        .startScript();
                executed = true;
            }
        }

        return executed;
    }
}
