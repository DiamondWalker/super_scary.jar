package diamondwalker.sscary.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ChatUtil {
    public static final String FRIEND_NAME = "Friend";
    public static final String CORRUPTED_ENTITY_NAME = "c0rrup1e3_en1i1y";
    public static final String CALCULATION_NAME = "Calculation";

    public static MutableComponent getJoinMessage(String name) {
        return Component.translatable("multiplayer.player.joined", name).withStyle(ChatFormatting.YELLOW);
    }

    public static MutableComponent getLeaveMessage(String name) {
        return Component.translatable("multiplayer.player.left", name).withStyle(ChatFormatting.YELLOW);
    }

    public static MutableComponent getEntityChatMessage(String name, String msg) {
        return Component.literal("<" + name + "> " + msg);
    }
}
