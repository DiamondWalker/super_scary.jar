package diamondwalker.sscary.randomevent.rare;

import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.util.ChatUtil;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class JoinAndLeaveEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        ScriptBuilder builder = new ScriptBuilder(server, "join-leave");

        Component join = ChatUtil.getJoinMessage("");
        Component leave = ChatUtil.getLeaveMessage("");
        boolean isJoin = true;
        boolean duplicate = false;

        for (int i = 0; i < 40; i++) {
            builder.chatMessageForAll(isJoin ? join : leave);

            float progress = ((float) i) / 40;
            int interval = (int) (Math.pow(1.0f - progress, 4) * 120);
            builder.rest(Math.max(1, interval));

            if (duplicate) {
                duplicate = false;
            } else {
                isJoin = !isJoin;
                duplicate = progress > 0.4f && server.overworld().getRandom().nextInt(4) == 0;
            }
        }

        builder.startScript();

        return true;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.RARE;
    }
}
