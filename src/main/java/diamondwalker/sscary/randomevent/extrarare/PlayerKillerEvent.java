package diamondwalker.sscary.randomevent.extrarare;

import diamondwalker.sscary.data.server.WorldData;
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
        if (validPlayers.length > 0) {
            RandomSource random = server.overworld().getRandom();
            ServerPlayer player = validPlayers[random.nextInt(validPlayers.length)];

            WorldData data = WorldData.get(server);
            if (!data.playerKiller.hasPlayerBeenThreatened(player)) {
                String playerName = validPlayers[random.nextInt(validPlayers.length)].getDisplayName().getString();
                server.getPlayerList().broadcastSystemMessage(
                        ChatUtil.getEntityChatMessage(
                                "The" + playerName + "Killer",
                                "I am going to kill " + playerName + "."
                        ),
                        false
                );
                data.playerKiller.addThreatenedPlayer(player);
                return true;
            }
        }

        return false;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.EXTRA_RARE;
    }
}
