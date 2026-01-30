package diamondwalker.twais;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    /*public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);*/

    public static final ModConfigSpec.IntValue MAX_EVENT_INTERVAL = BUILDER
            .comment("The maximum time, in ticks, that will pass between random events.")
            .defineInRange("maxEventInterval", 20 * 60 * 20, 0, Integer.MAX_VALUE); // 20 minutes

    public static final ModConfigSpec.IntValue MED_EVENT_INTERVAL = BUILDER
            .comment("The number of ticks between random events will be under this value approximately 50% of the time.")
            .defineInRange("medEventInterval", 20 * 60 * 5,  0, Integer.MAX_VALUE); // 5 minutes

    public static final ModConfigSpec.IntValue MIN_EVENT_INTERVAL = BUILDER
            .comment("The minimum time, in ticks, that will pass between random events.")
            .defineInRange("minEventInterval", 20 * 90,  0, Integer.MAX_VALUE); // 1.5 minutes

    /*public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);*/

    static final ModConfigSpec SPEC = BUILDER.build();

    /*private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }*/
}
