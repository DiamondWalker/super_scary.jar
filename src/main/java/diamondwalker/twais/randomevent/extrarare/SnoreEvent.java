package diamondwalker.twais.randomevent.extrarare;

import diamondwalker.twais.registry.TWAISSounds;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public class SnoreEvent {
    public static boolean execute(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(new ClientboundSoundPacket(TWAISSounds.SNORE, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 64.0F, 1.0F, server.overworld().getRandom().nextLong()));
        }

        return true;
    }
}
