package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.script.variable.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SScaryScriptVariables {
    public static final DeferredRegister<ScriptVariableType> SCRIPT_VARIABLE_TYPES = DeferredRegister.create(CustomRegistries.SCRIPT_VARIABLE_TYPE_REGISTRY, SScary.MODID);

    public static final Supplier<ScriptVariableType> BOOLEAN = SCRIPT_VARIABLE_TYPES.register("boolean", () -> BooleanVariable.Update::new);
    public static final Supplier<ScriptVariableType> INTEGER = SCRIPT_VARIABLE_TYPES.register("integer", () -> IntegerVariable.Update::new);
    public static final Supplier<ScriptVariableType> STRING = SCRIPT_VARIABLE_TYPES.register("string", () -> StringVariable.Update::new);
    public static final Supplier<ScriptVariableType> ENUM = SCRIPT_VARIABLE_TYPES.register("enum", () -> EnumVariable.Update::new);

    public static void register(IEventBus bus) {
        SCRIPT_VARIABLE_TYPES.register(bus);
    }
}
