package diamondwalker.twais.registry;

import diamondwalker.twais.TWAIS;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TWAISBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TWAIS.MODID);

    /*public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = TWAISItems.ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);*/

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
