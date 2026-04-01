package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SScarySounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, SScary.MODID);

    public static final Holder<SoundEvent> STATIC = SOUNDS.register("static", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> SNORE = SOUNDS.register("snore", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> WORLD_DARKEN = SOUNDS.register("world_darken", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> CALCULATION_PUNISHMENT = SOUNDS.register("calculation_punishment", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> PARTY_START = SOUNDS.register("party_start", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> PARTY = SOUNDS.register("party", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> FRIED_STEVE_MUSIC = SOUNDS.register("fried_steve_music", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> FRIED_STEVE_PRELUDE = SOUNDS.register("fried_steve_prelude", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> FRIED_STEVE_SPAWN = SOUNDS.register("fried_steve_spawn", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> FRIED_STEVE_JUMPSCARE = SOUNDS.register("fried_steve_jumpscare", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> DEADEYE_LOAD = SOUNDS.register("deadeye_load", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> DEADEYE_SHOOT = SOUNDS.register("deadeye_shoot", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> CORRUPTED_JUMPSCARE = SOUNDS.register("corrupted_jumpscare", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> CONSTRUCT = SOUNDS.register("construct", SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> VISAGE_SPAWN = SOUNDS.register("visage_spawn", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> VISAGE_CHASE = SOUNDS.register("visage_chase", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> VISAGE_TELEPORT = SOUNDS.register("visage_teleport", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> VISAGE_SCARE = SOUNDS.register("visage_scare", SoundEvent::createVariableRangeEvent);

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
