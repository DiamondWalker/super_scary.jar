package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.script.ScriptType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
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

    @SubscribeEvent
    private static void register(NewRegistryEvent event) {
        event.register(SCRIPT_REGISTRY);
    }
}
