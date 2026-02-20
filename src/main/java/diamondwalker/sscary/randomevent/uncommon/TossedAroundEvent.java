package diamondwalker.sscary.randomevent.uncommon;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.handler.internal.PlayerFallHandler;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class TossedAroundEvent {
    public static boolean execute(MinecraftServer server) {
        boolean executed = false;

        RandomSource random = server.overworld().getRandom();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            int duration = 60 + random.nextInt(140 + 1);
            ScriptBuilder builder = new ScriptBuilder(server);
            for (int i = 0; i < duration; i++) {
                builder.rest(1);
                builder.action((serv) -> {
                    double x = random.nextDouble() * 2 - 1;
                    double y = random.nextDouble() * 0.6;
                    double z = random.nextDouble() * 2 - 1;
                    Vec3 motion = new Vec3(x, y, z).scale(0.25);
                    player.addDeltaMovement(motion);
                    player.hurtMarked = true;

                    if (!Config.ULTRA_SCARY_MODE.get()) PlayerFallHandler.disableFall(player);
                });
            }
            builder.startScript();
            executed = true;
        }

        return executed;
    }
}
