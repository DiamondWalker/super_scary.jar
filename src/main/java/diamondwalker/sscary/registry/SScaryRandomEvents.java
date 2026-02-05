package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RegisteredEvent;
import diamondwalker.sscary.randomevent.common.*;
import diamondwalker.sscary.randomevent.extrarare.*;
import diamondwalker.sscary.randomevent.rare.*;
import diamondwalker.sscary.randomevent.uncommon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.util.function.Function;

import static diamondwalker.sscary.randomevent.EnumEventRarity.*;

public class SScaryRandomEvents {
    public static RegisteredEvent CORRUPTED_WATCHING;
    public static RegisteredEvent FIRE;
    public static RegisteredEvent INVALID_TEXT;
    public static RegisteredEvent LIGHTNING;
    public static RegisteredEvent MYSTERY_PERSON;
    public static RegisteredEvent STRUCTURE_SPAWN;
    public static RegisteredEvent CALCULATION;

    public static RegisteredEvent DROP_FROM_SKY;
    public static RegisteredEvent HEALTH_CHANGE;
    public static RegisteredEvent INVENTORY_SHUFFLE;
    public static RegisteredEvent ITEM_DROP;
    public static RegisteredEvent STATIC_TELEPORT;
    public static RegisteredEvent TOSSED_AROUND;

    public static RegisteredEvent DARK_WORLD;
    public static RegisteredEvent JOIN_AND_LEAVE;
    public static RegisteredEvent TOUCH_GRASS;
    public static RegisteredEvent SOUND_OF_DEATH;

    public static RegisteredEvent PARTY;
    public static RegisteredEvent SNORE;

    private static RegisteredEvent register(EnumEventRarity rarity, String location, Function<MinecraftServer, Boolean> func) {
        return new RegisteredEvent(rarity, ResourceLocation.fromNamespaceAndPath(SScary.MODID, location), func);
    }

    public static void registerRandomEvents() {
        CORRUPTED_WATCHING = register(COMMON, "corrupted_watching", CorruptedWatchingEvent::execute);
        FIRE = register(COMMON, "fire", FireEvent::execute);
        INVALID_TEXT = register(COMMON, "invalid_text", InvalidTextEvent::execute);
        LIGHTNING = register(COMMON, "lightning", LightningEvent::execute);
        MYSTERY_PERSON = register(COMMON, "mystery_person", MysteryPersonEvent::execute);
        STRUCTURE_SPAWN = register(COMMON, "structure_spawn", StructureSpawnEvent::execute);
        CALCULATION = register(UNCOMMON, "calculation", CalculationEvent::execute);

        DROP_FROM_SKY = register(UNCOMMON, "drop_from_sky", DropFromSkyEvent::execute);
        HEALTH_CHANGE = register(UNCOMMON, "health_change", HealthChangeEvent::execute);
        INVENTORY_SHUFFLE = register(UNCOMMON, "inventory_shuffle", InventoryShuffleEvent::execute);
        ITEM_DROP = register(UNCOMMON, "item_drop", ItemDropEvent::execute);
        STATIC_TELEPORT = register(UNCOMMON, "static_teleport", StaticTeleportEvent::execute);
        TOSSED_AROUND = register(UNCOMMON, "tossed_around", TossedAroundEvent::execute);

        DARK_WORLD = register(RARE, "dark_world", DarkWorldEvent::execute);
        JOIN_AND_LEAVE = register(RARE, "join_and_leave", JoinAndLeaveEvent::execute);
        TOUCH_GRASS = register(RARE, "touch_grass", TouchGrassEvent::execute);
        SOUND_OF_DEATH = register(COMMON, "sound_of_death", SoundOfDeathEvent::execute);

        PARTY = register(EXTRA_RARE, "party", PartyEvent::execute);
        SNORE = register(EXTRA_RARE, "snore", SnoreEvent::execute);
    }
}
