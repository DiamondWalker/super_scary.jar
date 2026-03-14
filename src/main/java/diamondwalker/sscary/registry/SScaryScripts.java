package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.script.*;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SScaryScripts {
    public static final DeferredRegister<ScriptType<?>> SCRIPTS = DeferredRegister.create(CustomRegistries.SCRIPT_REGISTRY, SScary.MODID);

    public static final Supplier<ScriptType<CalculationScript>> CALCULATION = SCRIPTS.register(
            "calculation",
            () -> new ScriptType<>(CalculationScript::new, CalculationScript::new)
    );

    public static final Supplier<ScriptType<FriedSteveScript>> FRIED_STEVE = SCRIPTS.register(
            "fried_steve",
            () -> new ScriptType<>(FriedSteveScript::new, FriedSteveScript::new)
    );

    public static final Supplier<ScriptType<BoatExplosionScript>> BOAT_EXPLOSION = SCRIPTS.register(
            "boat_explosion",
            () -> new ScriptType<>((server) -> new BoatExplosionScript(null, null, server)).noSave()
    );

    public static final Supplier<ScriptType<CorruptedMusicDiscScript>> CORRUPTED_MUSIC_DISC = SCRIPTS.register(
            "corrupted_music_disc",
            () -> new ScriptType<>(CorruptedMusicDiscScript::new) // TODO: when I implement locks/tags this hould be listed under corrupted_entity's
    );

    public static void register(IEventBus bus) {
        SCRIPTS.register(bus);
    }
}
