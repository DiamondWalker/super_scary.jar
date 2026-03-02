package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.script.CalculationScript;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.script.ScriptType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SScaryScripts {
    public static final DeferredRegister<ScriptType<?>> SCRIPTS = DeferredRegister.create(CustomRegistries.SCRIPT_REGISTRY, SScary.MODID);

    public static final Supplier<ScriptType<CalculationScript>> CALCULATION = SCRIPTS.register("calculation", () -> new ScriptType<>(CalculationScript::new));

    public static void register(IEventBus bus) {
        SCRIPTS.register(bus);
    }
}
