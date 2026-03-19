package diamondwalker.sscary.script;

import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.script.variable.IntegerVariable;
import diamondwalker.sscary.script.variable.StringVariable;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

public class NaNOffendedScript extends Script {
    private final IntegerVariable time = IntegerVariable.create().save("time").define(this);
    private final StringVariable name = StringVariable.create().save("name").defaultValue("Player").define(this);

    public NaNOffendedScript(Player sender) {
        this(sender.getServer());
        name.set(sender.getName().getString());
    }

    public NaNOffendedScript(MinecraftServer server) {
        super(SScaryScripts.NAN_OFFENDED.get(), server);
    }

    @Override
    public void tick() {
        if (time.get() == 60) {
            chatMessageForAll(ChatUtil.getEntityChatMessage("NaN", "§o" + name.get() + "...? Why would you say that to me... That is so mean ;_;"));
        } else if (time.get() == 180) {
            chatMessageForAll(ChatUtil.getEntityChatMessage("NaN", "§oWhy does everyone hate me... Am I really that annoying...?"));
        } else if (time.get() == 250) {
            chatMessageForAll(ChatUtil.getEntityChatMessage("NaN", "§oI'm sorry..."));
        } else if (time.get() == 330) {
            chatMessageForAll(ChatUtil.getEntityChatMessage("NaN", "§oI'll leave now so I don't bother anyone else..."));
        } else if (time.get() >= 390) {
            sendLeaveMessage("NaN");
            end();
        }

        time.set(time.get() + 1);
    }
}
