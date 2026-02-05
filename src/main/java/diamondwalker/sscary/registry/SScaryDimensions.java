package diamondwalker.sscary.registry;

import diamondwalker.sscary.PurgatorySpecialEffects;
import diamondwalker.sscary.SScary;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

import java.util.OptionalLong;

@EventBusSubscriber
public class SScaryDimensions {
    public static final ResourceLocation PURGATORY_EFFECTS = ResourceLocation.fromNamespaceAndPath(SScary.MODID, "purgatory");

    public static final ResourceKey<LevelStem> PURGATORY_KEY = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(SScary.MODID, "purgatory"));
    public static final ResourceKey<Level> PURGATORY_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(SScary.MODID, "purgatory"));
    public static final ResourceKey<DimensionType> PURGATORY_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(SScary.MODID, "purgatory"));


    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        context.register(PURGATORY_TYPE, new DimensionType(
                OptionalLong.of(18000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                true, // natural
                1.0, // coordinateScale
                true, // bedWorks
                true, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                PURGATORY_EFFECTS, // effectsLocation
                0.1f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(SScaryBiomes.PURGATORY)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(PURGATORY_TYPE), wrappedChunkGenerator);

        context.register(PURGATORY_KEY, stem);
    }

    @SubscribeEvent
    public static void registerEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(PURGATORY_EFFECTS, new PurgatorySpecialEffects());//new StillwoodsSpecialEffects());
    }
}
