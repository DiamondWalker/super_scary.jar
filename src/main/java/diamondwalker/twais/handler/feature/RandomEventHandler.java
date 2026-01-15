package diamondwalker.twais.handler.feature;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.randomevent.EnumEventRarity;
import diamondwalker.twais.randomevent.RandomEventRegistry;
import diamondwalker.twais.randomevent.RegisteredEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class RandomEventHandler {
    // TODO: test events to make sure they're still working. Maybe add a command to force them for easier testing?
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        RandomSource random = server.overworld().getRandom();
        WorldData data = WorldData.get(server);

        if (!data.progression.hasBeenAngered()) return;

        if (data.randomEvents.timeSinceLastEvent >= data.randomEvents.timeForNextEvent) {
            if (data.randomEvents.timeForNextEvent > 0) { // ensure this isn't the first time
                RegisteredEvent eventPick;
                do {
                    /*
                    Common - 69%
                    Uncommon - 25%
                    Rare - 5%
                    Extra Rare - 1%
                     */
                    float typeF = random.nextFloat();
                    EnumEventRarity type;
                    if (typeF < 0.01f) {
                        type = EnumEventRarity.EXTRA_RARE;
                    } else if (typeF < 0.06f) {
                        type = EnumEventRarity.RARE;
                    } else if (typeF < 0.31f) {
                        type = EnumEventRarity.UNCOMMON;
                    } else {
                        type = EnumEventRarity.COMMON;
                    }

                    eventPick = RandomEventRegistry.getRandomEventFromRarity(type, random);
                } while (!eventPick.function.apply(server));
            }

            data.randomEvents.timeSinceLastEvent = 0;
            // TODO: min interval, max interval, and exponent should be configurable
            double f = Math.pow(random.nextDouble(), 3.88); // exponent here is 3.88
            long maxInterval = 20 * 60 * 60; // one hour
            long minInterval = 20 * 60 * 1; // one minute
            long range = maxInterval - minInterval;
            data.randomEvents.timeForNextEvent = minInterval + Math.round(f * range);

        } else {
            data.randomEvents.timeSinceLastEvent++;
        }

        TWAIS.executeDebugCode(() -> System.out.println(data.randomEvents.timeForNextEvent - data.randomEvents.timeSinceLastEvent));
    }
}
