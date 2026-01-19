package diamondwalker.twais.randomevent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RandomEventRegistry {
    public static final HashMap<EnumEventRarity, ArrayList<RegisteredEvent>> MAP = new HashMap<>();
    private static final HashMap<ResourceLocation, RegisteredEvent> REGISTRY = new HashMap<>();

    public static RegisteredEvent getEventFromKey(ResourceLocation location) {
        return REGISTRY.get(location);
    }

    public static Collection<RegisteredEvent> getAllRegisteredEvents() {
        return REGISTRY.values();
    }

    public static RegisteredEvent getRandomEventFromRarity(EnumEventRarity rarity, RandomSource rand) {
        List<RegisteredEvent> list = MAP.get(rarity);
        return list.get(rand.nextInt(list.size()));
    }

    protected static void register(RegisteredEvent event) {
        if (REGISTRY.put(event.id, event) != null) {
            throw new IllegalStateException("Event with key " + event.id + " was already registered.");
        }

        if (!MAP.containsKey(event.rarity)) MAP.put(event.rarity, new ArrayList<>());
        MAP.get(event.rarity).add(event);
    }
}
