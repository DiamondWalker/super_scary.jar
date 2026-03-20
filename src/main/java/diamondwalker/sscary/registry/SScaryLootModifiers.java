package diamondwalker.sscary.registry;

import com.mojang.serialization.MapCodec;
import diamondwalker.sscary.AddLootItemModifier;
import diamondwalker.sscary.SScary;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SScaryLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SScary.MODID);

    public static final Supplier<MapCodec<AddLootItemModifier>> ADD_ITEM =
            GLOBAL_LOOT_MODIFIERS.register("add_item", () -> AddLootItemModifier.CODEC);

    public static void register(IEventBus bus) {
        GLOBAL_LOOT_MODIFIERS.register(bus);
    }
}
