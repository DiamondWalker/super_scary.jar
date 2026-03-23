package diamondwalker.sscary.item;

import diamondwalker.sscary.entity.projectile.pepperspray.EntityPepperSpray;
import diamondwalker.sscary.util.TooltipFiller;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.List;

public class PepperSprayItem extends Item {
    public PepperSprayItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        player.getCooldowns().addCooldown(this, 17);

        if (!level.isClientSide()) {
            EntityPepperSpray projectile = new EntityPepperSpray(level, player);
            projectile.setPos(player.getEyePosition());
            projectile.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 0.9f, 0);
            level.addFreshEntity(projectile);
            item.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        TooltipFiller tooltip = new TooltipFiller(this, tooltipComponents);

        tooltip.addLine((component -> component
                .withColor(FastColor.ARGB32.color(224, 150, 0))
                .withStyle(ChatFormatting.ITALIC)));

        tooltip.addLines(3, component -> component
                .withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));

        tooltip.addLineWithArguments(component -> component.withStyle(ChatFormatting.GRAY), stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage());
    }
}
