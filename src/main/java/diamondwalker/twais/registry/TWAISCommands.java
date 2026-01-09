package diamondwalker.twais.registry;

import diamondwalker.twais.command.AngerCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class TWAISCommands {
    @SubscribeEvent
    private static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(AngerCommand.build(event.getBuildContext()));
    }
}
