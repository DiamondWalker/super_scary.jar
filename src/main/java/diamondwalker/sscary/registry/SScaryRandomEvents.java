package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.randomevent.common.*;
import diamondwalker.sscary.randomevent.common.calculation.CalculationEvent;
import diamondwalker.sscary.randomevent.extrarare.*;
import diamondwalker.sscary.randomevent.rare.*;
import diamondwalker.sscary.randomevent.uncommon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

import static diamondwalker.sscary.randomevent.EnumEventRarity.*;

public class SScaryRandomEvents {
    public static final DeferredRegister<RandomEvent> RANDOM_EVENTS = DeferredRegister.create(CustomRegistries.RANDOM_EVENT_REGISTRY, SScary.MODID);

    public static final Supplier<RandomEvent> CORRUPTED_WATCHING = RANDOM_EVENTS.register("corrupted_watching", CorruptedWatchingEvent::new);
    public static final Supplier<RandomEvent> FIRE = RANDOM_EVENTS.register("fire", FireEvent::new);
    public static final Supplier<RandomEvent> INVALID_TEXT = RANDOM_EVENTS.register("invalid_text", InvalidTextEvent::new);
    public static final Supplier<RandomEvent> LIGHTNING = RANDOM_EVENTS.register("lightning", LightningEvent::new);
    public static final Supplier<RandomEvent> MYSTERY_PERSON = RANDOM_EVENTS.register("mystery_person", MysteryPersonEvent::new);
    public static final Supplier<RandomEvent> STRUCTURE_SPAWN = RANDOM_EVENTS.register("structure_spawn", StructureSpawnEvent::new);
    public static final Supplier<RandomEvent> CALCULATION = RANDOM_EVENTS.register("calculation", CalculationEvent::new);
    public static final Supplier<RandomEvent> DROP_FROM_SKY = RANDOM_EVENTS.register("drop_from_sky", DropFromSkyEvent::new);
    public static final Supplier<RandomEvent> HEALTH_CHANGE = RANDOM_EVENTS.register("health_change", HealthChangeEvent::new);
    public static final Supplier<RandomEvent> INVENTORY_SHUFFLE = RANDOM_EVENTS.register("inventory_shuffle", InventoryShuffleEvent::new);
    public static final Supplier<RandomEvent> ITEM_DROP = RANDOM_EVENTS.register("item_drop", ItemDropEvent::new);
    public static final Supplier<RandomEvent> STATIC_TELEPORT = RANDOM_EVENTS.register("static_teleport", StaticTeleportEvent::new);
    public static final Supplier<RandomEvent> TOSSED_AROUND = RANDOM_EVENTS.register("tossed_around", TossedAroundEvent::new);
    public static final Supplier<RandomEvent> DARK_WORLD = RANDOM_EVENTS.register("dark_world", DarkWorldEvent::new);
    public static final Supplier<RandomEvent> JOIN_AND_LEAVE = RANDOM_EVENTS.register("join_and_leave", JoinAndLeaveEvent::new);
    public static final Supplier<RandomEvent> TOUCH_GRASS = RANDOM_EVENTS.register("touch_grass", TouchGrassEvent::new);
    public static final Supplier<RandomEvent> SOUND_OF_DEATH = RANDOM_EVENTS.register("sound_of_death", SoundOfDeathEvent::new);
    public static final Supplier<RandomEvent> PARTY = RANDOM_EVENTS.register("party", PartyEvent::new);
    public static final Supplier<RandomEvent> SNORE = RANDOM_EVENTS.register("snore", SnoreEvent::new);

    public static void register(IEventBus bus) {
        RANDOM_EVENTS.register(bus);
    }
}
