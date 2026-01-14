package diamondwalker.twais.randomevent.common;

import diamondwalker.twais.entity.corrupted.EntityCorrupted;
import diamondwalker.twais.registry.TWAISEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;

public class CorruptedWatchingEvent {
    public static boolean execute(MinecraftServer server) {
        ServerLevel level = server.overworld();
        RandomSource random = level.getRandom();
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
                    return entity != null;
                }
            }
        }

        return false;
    }
}
