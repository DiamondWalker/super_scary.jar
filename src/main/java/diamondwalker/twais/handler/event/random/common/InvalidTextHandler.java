package diamondwalker.twais.handler.event.random.common;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.entity.corrupted.EntityCorrupted;
import diamondwalker.twais.entity.nametag.EntityNametag;
import diamondwalker.twais.registry.TWAISEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class InvalidTextHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (data.progression.hasBeenAngered() && !data.areEventsOnCooldown()) { // since this is a more passive event I don't think it needs the cooldown check
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(WorldData.COMMON_CHANCE) == 0) {
                ServerLevel level = server.overworld();
                for (ServerPlayer player : level.players()) {
                    if (player.isAlive()) {
                        do {
                            for (int tries = 0; tries < 100; tries++) {
                                double xOffset = random.nextDouble() * 100 - 50;
                                double yOffset = random.nextDouble() * 100 - 50;
                                double zOffset = random.nextDouble() * 100 - 50;

                                Vec3 offsetVector = new Vec3(xOffset, yOffset, zOffset);
                                if (offsetVector.length() > 40) {
                                    Vec3 pos = player.getEyePosition().add(offsetVector);
                                    EntityNametag nametag = TWAISEntities.NAMETAG.get().create(level);
                                    nametag.setPos(pos);
                                    nametag.setCustomName(Component.literal("Invalid"));
                                    level.addFreshEntity(nametag);
                                    data.eventCooldown();
                                    break;
                                }
                            }
                        } while (random.nextInt(3) == 0);
                    }
                }
            }
        }
    }
}
