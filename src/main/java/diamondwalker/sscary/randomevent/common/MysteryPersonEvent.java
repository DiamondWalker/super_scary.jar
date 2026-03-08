package diamondwalker.sscary.randomevent.common;

import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public class MysteryPersonEvent extends RandomEvent {
    public static final String[] MESSAGES = new String[] {
            "it hurts",
            "please",
            "im sorry",
            "just kill me already",
            "help",
            "help me",
            "why"
    };

    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        RandomSource random = server.overworld().getRandom();
        server.getPlayerList().broadcastSystemMessage(Component.literal(MESSAGES[random.nextInt(MESSAGES.length)]), false);
        return true;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.COMMON;
    }
}
