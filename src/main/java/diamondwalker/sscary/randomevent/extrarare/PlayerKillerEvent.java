package diamondwalker.sscary.randomevent.extrarare;

import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public class PlayerKillerEvent extends RandomEvent {
    @Override
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        RandomSource random = server.overworld().getRandom();
        String playerName = validPlayers[random.nextInt(validPlayers.length)].getDisplayName().getString();
        server.getPlayerList().broadcastSystemMessage(
                ChatUtil.getEntityChatMessage(
                        "The" + playerName + "Killer",
                        "I am going to kill " + playerName + "."
                ),
                false
        );
        return true;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.EXTRA_RARE;
    }
}
