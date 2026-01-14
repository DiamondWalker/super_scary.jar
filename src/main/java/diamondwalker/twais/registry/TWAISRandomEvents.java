package diamondwalker.twais.registry;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.randomevent.EnumEventRarity;
import diamondwalker.twais.randomevent.RegisteredEvent;
import diamondwalker.twais.randomevent.common.*;
import net.minecraft.resources.ResourceLocation;

public class TWAISRandomEvents {
    public static final RegisteredEvent CORRUPTED_WATCHING = new RegisteredEvent(EnumEventRarity.COMMON, ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "corrupted_watching"), CorruptedWatchingEvent::execute);
    public static final RegisteredEvent FIRE = new RegisteredEvent(EnumEventRarity.COMMON, ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "fire"), FireEvent::execute);
    public static final RegisteredEvent INVALID_TEXT = new RegisteredEvent(EnumEventRarity.COMMON, ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "invalid_text"), InvalidTextEvent::execute);
    public static final RegisteredEvent LIGHTNING = new RegisteredEvent(EnumEventRarity.COMMON, ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "lightning"), LightningEvent::execute);
    public static final RegisteredEvent MYSTERY_PERSON = new RegisteredEvent(EnumEventRarity.COMMON, ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "mystery_person"), MysteryPersonEvent::execute);
    public static final RegisteredEvent STRUCTURE_SPAWN = new RegisteredEvent(EnumEventRarity.COMMON, ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "structure_spawn"), StructureSpawnEvent::execute);


}
