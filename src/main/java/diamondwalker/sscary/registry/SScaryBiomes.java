package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class SScaryBiomes {
    public static final ResourceKey<Biome> PURGATORY = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(SScary.MODID, "purgatory"));

    public static void boostrap(BootstrapContext<Biome> context) {
        context.register(PURGATORY, testBiome(context));
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        //BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        //BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        //BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        //BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        //BiomeDefaultFeatures.addDefaultSprings(builder);
        //BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static Biome testBiome(BootstrapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        /*spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.RHINO.get(), 2, 3, 5));

        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));

        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);*/

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        //globalOverworldGeneration(biomeBuilder);
        //BiomeDefaultFeatures.addMossyStoneBlock(biomeBuilder);
        //BiomeDefaultFeatures.addForestFlowers(biomeBuilder);
        //BiomeDefaultFeatures.addFerns(biomeBuilder);
        //BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        //BiomeDefaultFeatures.addExtraGold(biomeBuilder);

        //biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_PLAINS);

        //BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        //BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);
        //biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PINE_PLACED_KEY);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.8f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x000000)
                        .waterFogColor(0x000000)
                        .skyColor(0xbfbfbf)
                        .grassColorOverride(0x212121)
                        .foliageColorOverride(0x212121)
                        .fogColor(0xffffff)
                        //.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(null)
                        .build())
                .build();
    }
}
