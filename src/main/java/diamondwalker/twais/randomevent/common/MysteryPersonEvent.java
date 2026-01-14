package diamondwalker.twais.randomevent.common;

import diamondwalker.twais.util.ChatUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public class MysteryPersonEvent {
    public static final String[] MESSAGES = new String[] {
            "I can see you.",
            "I can hear you.",
            "I can feel your heart beating.",
            "Behind you.",
            "You did this to us.",
            "Leave.",
            "I hate you.",
            "Why are you here?",
            "Look at me.",
            "It's all your fault.",
            "You skin is so soft. I can't wait to see it rip.",
            "Help.",
            "Please.",
            "You're running out of time.",
            "Give up.",
            "Do you feel safe?",
            "Can you see me?",
            "I'm here.",
            "It'll all be over soon."
            //"You forgot to flush the last time you went. I have fished your turd out of the toilet and I can't wait to use it on you."
    };

    public static boolean execute(MinecraftServer server) {
        RandomSource random = server.overworld().getRandom();
        server.getPlayerList().broadcastSystemMessage(ChatUtil.getEntityChatMessage(getName(random), getMessage(random)), false);
        return true;
    }

    private static String getName(RandomSource random) {
        int length = 4 + random.nextInt(7); // [4, 10]
        StringBuilder nameBuilder = new StringBuilder(length + 4);
        nameBuilder.append("§k");
        for (int i = 0; i < length; i++) nameBuilder.append('-');
        nameBuilder.append("§r");
        return nameBuilder.toString();
    }

    private static String getMessage(RandomSource random) {
        return MESSAGES[random.nextInt(MESSAGES.length)];
    }
}
