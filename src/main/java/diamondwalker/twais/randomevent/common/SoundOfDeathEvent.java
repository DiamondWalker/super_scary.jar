package diamondwalker.twais.randomevent.common;

import diamondwalker.twais.network.SilencePacket;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class SoundOfDeathEvent {
    public static boolean execute(MinecraftServer server) {
        new ScriptBuilder(server)
                .chatMessageForAll(Component.literal("This is what death sounds like."))
                .action((serv) -> PacketDistributor.sendToAllPlayers(new SilencePacket(true)))
                .rest(20 * 40)
                .action((serv) -> PacketDistributor.sendToAllPlayers(new SilencePacket(false)))
                .startScript();
        return true;
    }
}
