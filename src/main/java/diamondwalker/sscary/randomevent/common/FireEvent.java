package diamondwalker.sscary.randomevent.common;

import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class FireEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        boolean returnValue = false;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!player.isOnFire() && player.isAlive()) {
                player.setRemainingFireTicks(20 + server.overworld().getRandom().nextInt(40 + 1));
                returnValue = true;
            }
        }
        return returnValue;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.COMMON;
    }
}
