package diamondwalker.sscary.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.registry.CustomRegistries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class RandomEventCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext context) {
        return Commands.literal("randomevent")
                .requires((CommandSourceStack source) -> source.hasPermission(2))
                .then(Commands.argument("type", ResourceLocationArgument.id())
                        .suggests((((context1, builder) -> {
                            CustomRegistries.RANDOM_EVENT_REGISTRY.keySet()
                                    .forEach((key) -> builder.suggest(key.toString()));
                            return builder.buildFuture();
                        })))
                        .executes(RandomEventCommand::execute)
                );
    }

    protected static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (context.getSource().getLevel().dimension() != Level.OVERWORLD) {
            context.getSource().sendFailure(Component.literal("Random events do not currently function outside of the Overworld."));
            return 0;
        }

        ResourceLocation key = ResourceLocationArgument.getId(context, "type");

        RandomEvent event = CustomRegistries.RANDOM_EVENT_REGISTRY.get(key);
        if (event != null) {
            MinecraftServer server = context.getSource().getServer();
            ServerPlayer[] players = RandomEvent.getValidPlayers(server);
            if (players.length > 0) {
                event.execute(server, players);
                return Command.SINGLE_SUCCESS;
            } else {
                context.getSource().sendFailure(Component.literal("No valid players found for event execution"));
            }

        } else {
            context.getSource().sendFailure(Component.literal("No event exists with key " + key));
        }
        
        return 0;
    }
}
