package diamondwalker.twais.handler.event.random.rare;

import diamondwalker.twais.Config;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.network.DarkWorldPacket;
import diamondwalker.twais.network.VisageFlashPacket;
import diamondwalker.twais.registry.TWAISSounds;
import diamondwalker.twais.util.ScriptBuilder;
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
public class DarkWorldHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(Config.RARE_EVENT_CHANCE.getAsInt()) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    new ScriptBuilder(server)
                            .action((serv) -> {
                                player.connection.send(new ClientboundSoundPacket(TWAISSounds.WORLD_DARKEN, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 0.6F, random.nextLong()));
                                PacketDistributor.sendToPlayer(player, new DarkWorldPacket(true));
                            })
                            .rest(20 * (5 + random.nextInt(15)))
                            .action((serv) -> PacketDistributor.sendToPlayer(player, new DarkWorldPacket(false)))
                            .startScript();

                    data.eventCooldown();
                }
            }
        }
    }
}
