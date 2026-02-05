package diamondwalker.sscary.randomevent.common;

import diamondwalker.sscary.entity.nametag.EntityNametag;
import diamondwalker.sscary.registry.SScaryEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class InvalidTextEvent {
    public static boolean execute(MinecraftServer server) {
        boolean returnValue = false;

        ServerLevel level = server.overworld();
        RandomSource random = level.getRandom();
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
                            EntityNametag nametag = SScaryEntities.NAMETAG.get().create(level);
                            nametag.setPos(pos);
                            nametag.setCustomName(Component.literal("Invalid"));
                            level.addFreshEntity(nametag);
                            returnValue = true;
                            break;
                        }
                    }
                } while (random.nextInt(3) == 0);
            }
        }

        return returnValue;
    }
}
