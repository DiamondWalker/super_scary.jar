package diamondwalker.sscary.datagen;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.registry.SScaryBiomes;
import diamondwalker.sscary.registry.SScaryDimensions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SScaryWorldgenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, SScaryDimensions::bootstrapType)
            .add(Registries.LEVEL_STEM, SScaryDimensions::bootstrapStem)
            .add(Registries.BIOME, SScaryBiomes::boostrap);


    public SScaryWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(SScary.MODID));
    }
}
