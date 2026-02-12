package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.SScary;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class AnnoyingMultiplayerMessageHandler {
    private static int timer = 0;

    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        if (timer > 0) {
            timer--;
        } else {
            MinecraftServer server = event.getServer();
            if (server.getPlayerList().getPlayerCount() > 1) {
                server.getPlayerList().broadcastSystemMessage(Component.literal("!!!!!SUPER_SCARY.JAR DOES NOT CURRENTLY SUPPORT MULTIPLAYER! THINGS ARE GOING TO BREAK!!!!!").withStyle(ChatFormatting.DARK_RED), false);
                server.getPlayerList().broadcastSystemMessage(Component.literal("Please don't report multiplayer bugs as I'm already aware of them.").withStyle(ChatFormatting.DARK_RED), false);

                timer = 20 * 60 * 5;
            }
        }
    }
}
