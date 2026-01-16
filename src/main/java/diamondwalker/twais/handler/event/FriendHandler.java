package diamondwalker.twais.handler.event;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.ChatUtil;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class FriendHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);
        if (!data.friend.friendJoined && data.progression.getTimeInWorld() == 30_000L) {
            new ScriptBuilder(server, "friend")
                    .action((serv) -> WorldData.get(serv).friend.friendJoined = true)
                    .chatMessageForAll(ChatUtil.getJoinMessage("Friend"))
                    .rest(100)
                    .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Hello! I am your §efriend§r! I am here to help you in any way I can."))
                    .rest(60)
                    .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "I know a great deal of things. If you're ever feeling lost or confused, you can always §eask me a question§r!"))
                    .rest(80)
                    .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Make sure to start your question with the word \"friend\" so I can know you're speaking to me!"))
                    .startScript();
        }
    }

    @SubscribeEvent
    private static void handlePlayerChat(ServerChatEvent event) {
        String message = event.getMessage().getString();
        MinecraftServer server = event.getPlayer().getServer();
        if (message.length() >= 6 && message.substring(0, 6).equalsIgnoreCase("friend") && WorldData.get(server).friend.friendJoined) {
            ScriptBuilder sequence = new ScriptBuilder(event.getPlayer().getServer(), "friend");

            sequence.rest(70);

            WorldData data = WorldData.get(server);
            if (data.friend.friendLeft) {
                sequence
                        .chatMessageForAll(Component.literal("You don't have any friends."))//.chatMessageForAll(Component.literal(data.friend.friendDislikesYou ? "You don't have any friends." : "He can't help you now."))
                        .startScript();
                return;
            }

            message = message.substring(6).trim();
            int letterCount = 0;
            for (char c : message.toCharArray()) {
                if (Character.isLetter(c)) letterCount++;
            }
            if (letterCount <= 3) {
                sequence.
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "...what?"))
                        .startScript();
            } else if (!message.endsWith("?")) {
                sequence
                        .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Is that supposed to be a question? Then where is the §oquestion§r mark?"))
                        .action((serv) -> WorldData.get(serv).friend.friendDislikesYou = true)
                        .startScript();
            } else if (!message.startsWith(",")) {
                sequence
                        .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Friend, §lCOMMA§r, and §othen§r ask your question. I refuse to help people who type like neanderthals."))
                        .action((serv) -> WorldData.get(serv).friend.friendDislikesYou = true)
                        .startScript();
            } else {
                sequence
                        .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "How the hell am I supposed to know?!"))
                        .rest(30)
                        .chatMessageForAll(ChatUtil.getLeaveMessage("Friend"))
                        .action((serv) -> {
                            WorldData.get(serv).friend.friendLeft = true;
                            WorldData.get(serv).friend.friendDislikesYou = true;
                        })
                        .startScript();
            }
        }
    }
}
