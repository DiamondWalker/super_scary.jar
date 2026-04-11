package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.script.*;
import diamondwalker.sscary.script.randomevent.CalculationScript;
import diamondwalker.sscary.script.randomevent.DarkWorldScript;
import diamondwalker.sscary.script.randomevent.FriedSteveScript;
import diamondwalker.sscary.script.randomevent.PartyScript;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SScaryScripts {
    public static final DeferredRegister<ScriptType<?>> SCRIPTS = DeferredRegister.create(CustomRegistries.SCRIPT_REGISTRY, SScary.MODID);

    public static final Supplier<ScriptType<CorruptedIntroScript>> CORRUPTED_INTRO = SCRIPTS.register(
            "corrupted_intro",
            () -> new ScriptType<>(CorruptedIntroScript::new, CorruptedIntroScript::new)
    );

    public static final Supplier<ScriptType<CalculationScript>> CALCULATION = SCRIPTS.register(
            "calculation",
            () -> new ScriptType<>(CalculationScript::new, CalculationScript::new)
    );

    public static final Supplier<ScriptType<FriedSteveScript>> FRIED_STEVE = SCRIPTS.register(
            "fried_steve",
            () -> new ScriptType<>(FriedSteveScript::new, FriedSteveScript::new)
    );

    public static final Supplier<ScriptType<PartyScript>> PARTY = SCRIPTS.register(
            "party",
            () -> new ScriptType<>(PartyScript::new, PartyScript::new)
    );

    public static final Supplier<ScriptType<DarkWorldScript>> DARK_WORLD = SCRIPTS.register(
            "dark_world",
            () -> new ScriptType<>(DarkWorldScript::new, DarkWorldScript::new)
    );

    public static final Supplier<ScriptType<BoatExplosionScript>> BOAT_EXPLOSION = SCRIPTS.register(
            "boat_explosion",
            () -> new ScriptType<>((server) -> new BoatExplosionScript(null, null, server)).noSave()
    );

    public static final Supplier<ScriptType<CorruptedMusicDiscScript>> CORRUPTED_MUSIC_DISC = SCRIPTS.register(
            "corrupted_music_disc",
            () -> new ScriptType<>(CorruptedMusicDiscScript::new) // TODO: when I implement locks/tags this should be listed under corrupted_entity's
    );

    public static final Supplier<ScriptType<NaNOffendedScript>> NAN_OFFENDED = SCRIPTS.register(
            "nan_offended",
            () -> new ScriptType<>(NaNOffendedScript::new)
    );

    public static void register(IEventBus bus) {
        SCRIPTS.register(bus);
    }
}
