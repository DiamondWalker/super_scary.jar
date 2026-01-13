package diamondwalker.twais.handler.event.random.common;

import diamondwalker.twais.Config;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.entity.corrupted.EntityCorrupted;
import diamondwalker.twais.registry.TWAISEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class CorruptedWatchingHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (data.progression.hasBeenAngered() && !data.areEventsOnCooldown()) { // since this is a more passive event I don't think it needs the cooldown check
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(Config.COMMON_EVENT_CHANCE.getAsInt()) == 0) {
                ServerLevel level = server.overworld();
                for (ServerPlayer player : level.players()) {
                    if (player.isAlive()) {
                        for (int i = 0; i < 10; i++) {
                            double radius = random.nextDouble() * 80 + 80;
                            double angle = random.nextDouble() * Math.PI * 2;
                            double xd = player.getX() + Math.cos(angle) * radius;
                            double zd = player.getZ() + Math.sin(angle) * radius;
                            int x = (int) Math.round(xd);
                            int z = (int) Math.round(zd);
                            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                            if (y <= level.getSeaLevel()) continue;
                            BlockPos spawnPos = new BlockPos(x, y, z);
                            EntityCorrupted entity = TWAISEntities.CORRUPTED.get().spawn(level, spawnPos, MobSpawnType.MOB_SUMMONED);
                            if (entity != null) {
                                data.eventCooldown();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
