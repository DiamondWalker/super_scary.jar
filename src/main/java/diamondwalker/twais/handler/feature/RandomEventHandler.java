package diamondwalker.twais.handler.feature;

import diamondwalker.twais.Config;
import diamondwalker.twais.TWAIS;
import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.randomevent.EnumEventRarity;
import diamondwalker.twais.randomevent.RandomEventRegistry;
import diamondwalker.twais.randomevent.RegisteredEvent;
import diamondwalker.twais.util.MathUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
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

        if (
                Config.MAX_EVENT_INTERVAL.getAsInt() != data.randomEvents.prevMax ||
                Config.MIN_EVENT_INTERVAL.getAsInt() != data.randomEvents.prevMin ||
                Config.MED_EVENT_INTERVAL.getAsInt() != data.randomEvents.prevMed
        ) {
            TWAIS.LOGGER.info("Detected changes in event interval config. Next event time will be recalculated.");
            data.randomEvents.prevMax = Config.MAX_EVENT_INTERVAL.getAsInt();
            data.randomEvents.prevMin = Config.MIN_EVENT_INTERVAL.getAsInt();
            data.randomEvents.prevMed = Config.MED_EVENT_INTERVAL.getAsInt();
            refreshEventTime(data, random, false);
        }

        if (data.randomEvents.timeSinceLastEvent >= data.randomEvents.timeForNextEvent) {
            // if visage spawn is ready, it might happen instead of the event
            if (data.visage.spawnTicks >= 20 * 60 * 23 && random.nextBoolean()) { // after 23 minutes, the visage can spawn
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
        refreshEventTime(data, random, true);
    }

    public static void refreshEventTime(WorldData data, RandomSource random, boolean resetTimer) {
        if (resetTimer) data.randomEvents.timeSinceLastEvent = 0;

        data.randomEvents.timeForNextEvent = randomTimeFunction(random.nextDouble());
    }

    public static int randomTimeFunction(double input) {
        // retrieve the config values for convenience
        int max = Config.MAX_EVENT_INTERVAL.getAsInt();
        int min = Config.MIN_EVENT_INTERVAL.getAsInt();
        int median = Config.MED_EVENT_INTERVAL.getAsInt();

        // make sure user hasn't done something stupid
        if (max < min) max = min;
        median = Mth.clamp(median, min, max);
        if (min == max) return min;

        // We will create a function in the form a + bx^n
        // This function takes a random value x in the range [0, 1] and produces an output in the specified range [min, max]
        // Furthermore, an input of x = 0.5 should return the median value
        // a = min
        // b = max - min (the distance between our maximum and minimum values)
        int range = max - min;
        // Now our desired [min, max] range will be satisfied as long as 0 <= x^n <= 1
        // Luckily, functions in the form x^n always have a range [0, 1] when bounded to 0 <= x <= 1 (which ours is)
        // That means all we need to do now is ensure a + b * 0.5^n = median
        // We first solve for 0.5^n
        double medianFraction = (double)(median - min) / range; // 0.5^n = (median - a) / b. This is also the fraction of our range that falls under the median
        // now we solve for the exponent n using a logarithm base 0.5
        double exponent = MathUtil.logBase(medianFraction, 0.5);

        // we have our values for a, b, and n. We now run the original function with a random x
        return (int)Math.round(min + Math.pow(input, exponent) * range);
    }
}
