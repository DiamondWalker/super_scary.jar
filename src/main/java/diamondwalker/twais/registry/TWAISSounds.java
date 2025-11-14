package diamondwalker.twais.registry;

import diamondwalker.twais.TWAIS;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TWAISSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, TWAIS.MODID);

    public static final Holder<SoundEvent> SNORE = SOUNDS.register("snore", SoundEvent::createVariableRangeEvent);

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
