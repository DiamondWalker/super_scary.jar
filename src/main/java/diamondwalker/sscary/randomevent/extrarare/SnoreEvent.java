package diamondwalker.sscary.randomevent.extrarare;

import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public class SnoreEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(new ClientboundSoundPacket(SScarySounds.SNORE, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 64.0F, 1.0F, server.overworld().getRandom().nextLong()));
        }

        return true;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.EXTRA_RARE;
    }
}
