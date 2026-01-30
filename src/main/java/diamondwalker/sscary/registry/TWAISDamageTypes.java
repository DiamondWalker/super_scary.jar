package diamondwalker.sscary.registry;

import diamondwalker.sscary.TWAIS;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class TWAISDamageTypes {
    private static final ResourceKey<DamageType> CALCULATION =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "calculation"));

    public static DamageSource calculation(Entity victim) {
        return new DamageSource(
                victim.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(CALCULATION),
                null,
                null,
                null
        );
    }
}
