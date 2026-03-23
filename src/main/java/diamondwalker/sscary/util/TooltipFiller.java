package diamondwalker.sscary.util;

import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TooltipFiller {
    private int index = 0;
    private final String key;
    private final List<Component> tooltip;

    public TooltipFiller(Item item, List<Component> tooltip) {
        this.key = item.getDescriptionId() + ".description_";
        this.tooltip = tooltip;
    }

    public void addLine() {
        tooltip.add(Component.translatable(key + index++));
    }

    public void addLine(Function<MutableComponent, MutableComponent> formatting) {
        tooltip.add(formatting.apply(Component.translatable(key + index++)));
    }

    public void addLines(int lines) {
        for (int i = 0; i < lines; i++) {
            addLine();
        }
    }

    public void addLines(int lines, Function<MutableComponent, MutableComponent> formatting) {
        for (int i = 0; i < lines; i++) {
            addLine(formatting);
        }
    }

    public void addLineWithArguments(Object... args) {
        tooltip.add(Component.translatable(key + index++, args));
    }

    public void addLineWithArguments(Function<MutableComponent, MutableComponent> formatting, Object... args) {
        tooltip.add(formatting.apply(Component.translatable(key + index++, args)));
    }
}
