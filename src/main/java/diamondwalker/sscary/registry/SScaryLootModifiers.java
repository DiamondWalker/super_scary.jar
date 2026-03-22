package diamondwalker.sscary.registry;

import com.mojang.serialization.MapCodec;
import diamondwalker.sscary.lootmodifier.AddItemLootModifier;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.lootmodifier.ReplaceItemLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SScaryLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SScary.MODID);

    public static final Supplier<MapCodec<AddItemLootModifier>> ADD_ITEM =
            GLOBAL_LOOT_MODIFIERS.register("add_item", () -> AddItemLootModifier.CODEC);

    public static final Supplier<MapCodec<ReplaceItemLootModifier>> REPLACE_ITEM =
            GLOBAL_LOOT_MODIFIERS.register("replace_item", () -> ReplaceItemLootModifier.CODEC);

    public static void register(IEventBus bus) {
        GLOBAL_LOOT_MODIFIERS.register(bus);
    }
}
