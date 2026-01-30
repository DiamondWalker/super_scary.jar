package diamondwalker.sscary.randomevent.rare;

import diamondwalker.sscary.network.DarkWorldPacket;
import diamondwalker.sscary.registry.TWAISSounds;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.network.PacketDistributor;

public class DarkWorldEvent {
    public static boolean execute(MinecraftServer server) {
        boolean executed = false;

        RandomSource random = server.overworld().getRandom();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            new ScriptBuilder(server)
                    .action((serv) -> {
                        player.connection.send(new ClientboundSoundPacket(TWAISSounds.WORLD_DARKEN, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 0.6F, random.nextLong()));
                        PacketDistributor.sendToPlayer(player, new DarkWorldPacket(true));
                    })
                    .rest(20 * (5 + random.nextInt(15)))
                    .action((serv) -> PacketDistributor.sendToPlayer(player, new DarkWorldPacket(false)))
                    .startScript();

            executed = true;
        }

        return executed;
    }
}
