package diamondwalker.twais.handler.event.random;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.network.DarkWorldPacket;
import diamondwalker.twais.network.StaticScreenPacket;
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
public class StaticTeleportHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(WorldData.UNCOMMON_CHANCE) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    for (int i = 0; i < 20; i++) {
                        double x = player.getX() + random.nextDouble() * 40 - 20;
                        double y = player.getY() + random.nextDouble() * 40 - 20;
                        double z = player.getZ() + random.nextDouble() * 40 - 20;
                        if (player.randomTeleport(x, y, z, false)) {
                            PacketDistributor.sendToPlayer(player, new StaticScreenPacket(15));
                            data.eventCooldown();
                            break;
                        }
                    }
                }
            }
        }
    }
}
