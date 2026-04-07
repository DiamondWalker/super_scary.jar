package diamondwalker.sscary.item;

import diamondwalker.sscary.util.TooltipFiller;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class InventoryBugItem extends Item {
    public InventoryBugItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        new TooltipFiller(this, tooltipComponents).addLine(c -> c
                .withColor(FastColor.ARGB32.color(0, 255, 0))
                .withStyle(ChatFormatting.ITALIC));
    }
}
