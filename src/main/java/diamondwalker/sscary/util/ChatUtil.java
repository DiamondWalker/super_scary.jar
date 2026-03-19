package diamondwalker.sscary.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public static String getPlayerNickname(Player player) {
        String name = player.getDisplayName().getString();
        List<String> names = new Scanner(name)
                .findAll(Pattern.compile("([A-Z]+)?([a-z]+)?"))
                .map(MatchResult::group)
                .filter(string -> string.length() > 3)
                .toList();
        if (!names.isEmpty()) return names.getFirst();

        StringBuilder builder = new StringBuilder(name);
        while (!Character.isLetter(builder.charAt(0))) builder.deleteCharAt(0);
        while (!Character.isLetter(builder.charAt(builder.length() - 1))) builder.deleteCharAt(builder.length() - 1);
        if (builder.length() > 3) return builder.toString();

        return name;
    }
}
