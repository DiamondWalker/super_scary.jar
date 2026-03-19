package diamondwalker.sscary.handler.event;

import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.util.ChatUtil;
import diamondwalker.sscary.util.ScriptBuilder;
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
        if (!data.friend.friendJoined && data.progression.getTimeInWorld() == 9_600L && !data.progression.hasBeenAngered()) {
            new ScriptBuilder(server, "friend")
                    .action((serv) -> WorldData.get(serv).friend.friendJoined = true)
                    .chatMessageForAll(ChatUtil.getJoinMessage(ChatUtil.FRIEND_NAME))
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
            boolean properlyCapitalized = message.startsWith("Friend");

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
            } else if (!properlyCapitalized) {
                sequence
                        .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "\"Friend\". Uppercase \"F\", lowercase \"r\", \"i\", \"e\", \"n\", and \"d\". You should have learned this in elementary school."))
                        .action((serv) -> WorldData.get(serv).friend.friendDislikesYou = true)
                        .startScript();
            } else if (!message.startsWith(",")) {
                sequence
                        .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Friend, §lCOMMA§r, and §othen§r ask your question. I refuse to help people who type like neanderthals."))
                        .action((serv) -> WorldData.get(serv).friend.friendDislikesYou = true)
                        .startScript();
            } else if (!message.matches(", ?([a-z]|I ).+")) {
                sequence
                        .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "You do not capitalize after a comma. Good god..."))
                        .action((serv) -> WorldData.get(serv).friend.friendDislikesYou = true)
                        .startScript();
            } else {
                int i = 0;
                while (i < message.length()) {
                    if (Character.isLetter(message.charAt(i))) {
                        break;
                    }
                    i++;
                }
                // the "...what?" response already verifies we have characters so we don't have to check
                String question = message.substring(i, message.length() - 1);
                if (question.equalsIgnoreCase("where are you")) {
                    sequence
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "What an odd question. I am in " + server.getWorldData().getLevelName() + ". How else would I be speaking to you?"))
                            .startScript();
                } else if (question.equalsIgnoreCase("who are you")) {
                    sequence
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Are you illiterate? I already introduced myself."))
                            .startScript();
                } else if (question.toLowerCase().matches("how are you( feelin[g']?)?")) {
                    sequence
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Irritated. Do you intend on asking anything meaningful, or will you continue to waste my time with idle chatter?"))
                            .startScript();
                } else if (question.toLowerCase().matches("how are you doin([g'])?")) {
                    sequence
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Feeling irritated. Do you intend on asking anything meaningful, or will you continue to waste my time with idle chatter?"))
                            .startScript();
                } else if (question.toLowerCase().matches("why are you( being)?( so)? rude")) {
                    sequence
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "I'm not rude. I'm honest. There's a difference."))
                            .rest(65)
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Perhaps if you were slightly more intelligent, you would understand."))
                            .rest(30)
                            .chatMessageForAll(ChatUtil.getLeaveMessage(ChatUtil.FRIEND_NAME))
                            .startScript();
                } else {
                    sequence
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "How the hell am I supposed to know?!"))
                            .rest(30)
                            .chatMessageForAll(ChatUtil.getLeaveMessage(ChatUtil.FRIEND_NAME))
                            .action((serv) -> {
                                WorldData.get(serv).friend.friendLeft = true;
                                WorldData.get(serv).friend.friendDislikesYou = true;
                            })
                            .startScript();
                }
            }
        }
    }
}
