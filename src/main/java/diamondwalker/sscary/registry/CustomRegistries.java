package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.script.ScriptType;
import diamondwalker.sscary.script.variable.ScriptVariableType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

@EventBusSubscriber
public class CustomRegistries {
    public static final ResourceKey<Registry<ScriptType<?>>> SCRIPT_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "scripts"));
    public static final Registry<ScriptType<?>> SCRIPT_REGISTRY = new RegistryBuilder<>(SCRIPT_REGISTRY_KEY)
            .sync(true) // If you want to active integer id syncing, for networking.
            .create();

    public static final ResourceKey<Registry<ScriptVariableType>> SCRIPT_VARIABLE_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "script_variables"));
    public static final Registry<ScriptVariableType> SCRIPT_VARIABLE_TYPE_REGISTRY = new RegistryBuilder<>(SCRIPT_VARIABLE_TYPE_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<RandomEvent>> RANDOM_EVENT_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "random_event"));
    public static final Registry<RandomEvent> RANDOM_EVENT_REGISTRY = new RegistryBuilder<>(RANDOM_EVENT_REGISTRY_KEY)
            .create();

    @SubscribeEvent
    private static void register(NewRegistryEvent event) {
        event.register(SCRIPT_REGISTRY);
        event.register(SCRIPT_VARIABLE_TYPE_REGISTRY);
        event.register(RANDOM_EVENT_REGISTRY);
    }
}
