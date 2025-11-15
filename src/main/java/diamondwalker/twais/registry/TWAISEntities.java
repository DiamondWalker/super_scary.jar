package diamondwalker.twais.registry;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.entity.EntityVisage;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TWAISEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TWAIS.MODID);

    public static final Supplier<EntityType<EntityVisage>> VISAGE = ENTITY_TYPES.register("visage", () -> EntityType.Builder.of(
            EntityVisage::new,
            MobCategory.CREATURE
            )
            .sized(1.0f, 1.0f)
            .eyeHeight(0.5f)
            .build("visage")
    );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
