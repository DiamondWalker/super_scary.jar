package diamondwalker.twais.handler.event.random.extrarare;

import diamondwalker.twais.Config;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.registry.TWAISSounds;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class SnoreHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(Config.EXTRA_RARE_EVENT_CHANCE.getAsInt()) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    player.connection.send(new ClientboundSoundPacket(TWAISSounds.SNORE, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 64.0F, 1.0F, random.nextLong()));
                }
                data.eventCooldown();
            }
        }
    }
}
