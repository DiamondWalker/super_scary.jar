package diamondwalker.sscary.randomevent.extrarare;

import diamondwalker.sscary.network.PartyTimePacket;
import diamondwalker.sscary.registry.TWAISSounds;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.PacketDistributor;

public class PartyEvent {
    public static boolean execute(MinecraftServer server) {
        new ScriptBuilder(server)
                .chatMessageForAll(Component.literal("PARTY TIME!!!!"))
                .action((serv) -> {
                    for (ServerPlayer player : serv.getPlayerList().getPlayers()) {
                        player.connection.send(new ClientboundSoundPacket(TWAISSounds.PARTY_START, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 0.6F, serv.overworld().getRandom().nextLong()));
                    }
                })
                .rest(40)
                .action((serv -> {
                    for (ServerPlayer player : serv.getPlayerList().getPlayers()) {
                        PacketDistributor.sendToPlayer(player, new PartyTimePacket(true));
                    }
                }))
                .rest(1000)
                .action((serv -> {
                    for (ServerPlayer player : serv.getPlayerList().getPlayers()) {
                        PacketDistributor.sendToPlayer(player, new PartyTimePacket(false));
                    }
                }))
                .startScript();
        return true;
    }
}
