package diamondwalker.sscary.item;

import diamondwalker.sscary.entity.projectile.pepperspray.EntityPepperSpray;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
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

        tooltipComponents.add(Component.literal("Protect yourself from otherworldly assailants") // TODO: assailants twice? That's weird
                .withColor(FastColor.ARGB32.color(224, 150, 0))
                .withStyle(ChatFormatting.ITALIC));

        String[] lines = new String[] {
                "INSTRUCTIONS:",
                "Aim at assailant's face. Spray from ear to ear, across the eyes.",
                "Flee immediately and call the authorities." // TODO: check this writing
        };
        for (String line : lines) tooltipComponents.add(Component.literal(line).withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
        tooltipComponents.add(Component.literal("Uses: " + (stack.getMaxDamage() - stack.getDamageValue()) + "/" + stack.getMaxDamage()).withStyle(ChatFormatting.GRAY));
    }
}
