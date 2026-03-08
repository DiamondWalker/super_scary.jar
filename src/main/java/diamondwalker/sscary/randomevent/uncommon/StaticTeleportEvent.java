package diamondwalker.sscary.randomevent.uncommon;

import diamondwalker.sscary.network.StaticScreenPacket;
import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.network.PacketDistributor;

public class StaticTeleportEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] players) {
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

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.UNCOMMON;
    }
}
