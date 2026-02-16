package diamondwalker.sscary.randomevent.common;

import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;

public class MysteryPersonEvent {
    public static final String[] MESSAGES = new String[] {
            "it hurts",
            "please",
            "im sorry",
            "just kill me already",
            "help",
            "help me",
            "why"
    };

    public static boolean execute(MinecraftServer server) {
        RandomSource random = server.overworld().getRandom();
        server.getPlayerList().broadcastSystemMessage(Component.literal(MESSAGES[random.nextInt(MESSAGES.length)]), false);
        return true;
    }
}
