package diamondwalker.sscary.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import diamondwalker.sscary.data.server.WorldData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;

public class AngerCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext context) {
        return Commands.literal("startanger")
                .requires((CommandSourceStack source) -> source.hasPermission(2))
                .executes(AngerCommand::execute);
    }

    protected static int execute(CommandContext<CommandSourceStack> context) {
        MinecraftServer server = context.getSource().getServer();
        WorldData data = WorldData.get(server);
        if (!data.progression.hasBeenAngered()) {
            data.progression.startAnger(server);
        }
        return Command.SINGLE_SUCCESS;
    }
}
