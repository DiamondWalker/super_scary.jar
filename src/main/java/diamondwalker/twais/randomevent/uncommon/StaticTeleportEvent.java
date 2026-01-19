package diamondwalker.twais.randomevent.uncommon;

import diamondwalker.twais.network.StaticScreenPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.network.PacketDistributor;

public class StaticTeleportEvent {
    public static boolean execute(MinecraftServer server) {
        boolean executed = false;

        RandomSource random = server.overworld().getRandom();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            for (int i = 0; i < 20; i++) {
                double x = player.getX() + random.nextDouble() * 40 - 20;
                double y = player.getY() + random.nextDouble() * 40 - 20;
                double z = player.getZ() + random.nextDouble() * 40 - 20;
                if (player.randomTeleport(x, y, z, false)) {
                    PacketDistributor.sendToPlayer(player, new StaticScreenPacket(15));
                    executed = true;
                    break;
                }
            }
        }

        return executed;
    }
}
