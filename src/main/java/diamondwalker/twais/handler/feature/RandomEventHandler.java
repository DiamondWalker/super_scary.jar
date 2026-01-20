package diamondwalker.twais.handler.feature;

import diamondwalker.twais.Config;
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
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        RandomSource random = server.overworld().getRandom();
        WorldData data = WorldData.get(server);

        if (!data.progression.hasBeenAngered()) return;

        if (data.randomEvents.timeSinceLastEvent >= data.randomEvents.timeForNextEvent) {
            // if visage spawn is ready, it might happen instead of the event
            if (TWAIS.DEV_MODE && data.visage.spawnTicks >= 20 * 60 * 23 && random.nextBoolean()) { // after 23 minutes, the visage can spawn
                VisageHandler.spawnVisage(server);
                data.visage.spawnTicks = 0;
            } else {
                // random event
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

            refreshEventTime(data, random);
        } else {
            data.randomEvents.timeSinceLastEvent++;
        }

        if (TWAIS.DEV_MODE) System.out.println("Time left for random event: " + (data.randomEvents.timeForNextEvent - data.randomEvents.timeSinceLastEvent));
    }

    public static void refreshEventTime(WorldData data, RandomSource random) {
        data.randomEvents.timeSinceLastEvent = 0;
        double f = Math.pow(random.nextDouble(), Config.RANDOM_INTERVAL_EXPONENT.getAsDouble()); // exponent here is 3.88
        long maxInterval = Config.MAX_EVENT_INTERVAL.getAsInt(); // one hour
        long minInterval = Config.MIN_EVENT_INTERVAL.getAsInt(); // one minute
        long range = maxInterval - minInterval;
        data.randomEvents.timeForNextEvent = minInterval + Math.round(f * range);
    }
}
