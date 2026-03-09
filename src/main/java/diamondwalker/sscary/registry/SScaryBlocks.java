package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SScaryBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SScary.MODID);

    public static final DeferredBlock<Block> TOO_LAZY_TO_SPRITE_THIS = BLOCKS.registerSimpleBlock("too_lazy_to_sprite_this", BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.5F, 6.0F));
    public static final DeferredItem<BlockItem> TOO_LAZY_TO_SPRITE_THIS_ITEM = SScaryItems.ITEMS.registerSimpleBlockItem("too_lazy_to_sprite_this", TOO_LAZY_TO_SPRITE_THIS);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
