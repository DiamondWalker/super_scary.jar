package diamondwalker.sscary.randomevent;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public abstract class RandomEvent {
    public abstract boolean execute(MinecraftServer server, ServerPlayer[] validPlayers);

    public abstract EnumEventRarity getRarity();

    public static ServerPlayer[] getValidPlayers(MinecraftServer server) {
        return server.getPlayerList().getPlayers()
                .stream().filter((player) -> player.level().dimension() == Level.OVERWORLD)
                .toArray(ServerPlayer[]::new);
    }
}
