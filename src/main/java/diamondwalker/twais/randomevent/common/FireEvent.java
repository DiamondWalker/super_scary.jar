package diamondwalker.twais.randomevent.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class FireEvent {
    public static boolean execute(MinecraftServer server) {
        boolean returnValue = false;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!player.isOnFire() && player.isAlive()) {
                player.setRemainingFireTicks(20 + server.overworld().getRandom().nextInt(40 + 1));
                returnValue = true;
            }
        }
        return returnValue;
    }
}
