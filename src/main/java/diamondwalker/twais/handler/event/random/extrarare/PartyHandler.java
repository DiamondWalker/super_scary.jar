package diamondwalker.twais.handler.event.random.extrarare;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.network.PartyTimePacket;
import diamondwalker.twais.network.StaticScreenPacket;
import diamondwalker.twais.registry.TWAISSounds;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class PartyHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(WorldData.EXTRA_RARE_CHANCE) == 0) {
                new ScriptBuilder(server)
                        .chatMessageForAll(Component.literal("PARTY TIME!!!!"))
                        .action((serv) -> {
                            for (ServerPlayer player : serv.getPlayerList().getPlayers()) {
                                player.connection.send(new ClientboundSoundPacket(TWAISSounds.PARTY_START, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 0.6F, random.nextLong()));
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
                            data.eventCooldown(); // do it again because we've wasted a bit of time during the party
                        }))
                        .startScript();

                data.eventCooldown();
            }
        }
    }
}
