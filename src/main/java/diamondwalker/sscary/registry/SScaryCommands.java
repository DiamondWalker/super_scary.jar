package diamondwalker.sscary.registry;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import diamondwalker.sscary.command.AngerCommand;
import diamondwalker.sscary.command.PurgatoryTeleportCommand;
import diamondwalker.sscary.command.RandomEventCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.function.Function;

@EventBusSubscriber
public class SScaryCommands {
    @SubscribeEvent
    private static void registerCommands(RegisterCommandsEvent event) {
        registerAll(event.getDispatcher(), event.getBuildContext(),
                AngerCommand::build,
                RandomEventCommand::build,
                PurgatoryTeleportCommand::build
        );
    }

    @SafeVarargs
    private static void registerAll(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Function<CommandBuildContext, LiteralArgumentBuilder<CommandSourceStack>>... commands) {
        for (Function<CommandBuildContext, LiteralArgumentBuilder<CommandSourceStack>> command : commands) {
            dispatcher.register(command.apply(context));
        }
    }
}
