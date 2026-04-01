package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.entity.entity.watchtower.EntityWatchtower;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SScaryDamageTypes {
    private static final ResourceKey<DamageType> CALCULATION =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SScary.MODID, "calculation"));

    private static final ResourceKey<DamageType> MIGRAINE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SScary.MODID, "migraine"));

    private static final ResourceKey<DamageType> GUN =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SScary.MODID, "gun"));

    public static DamageSource calculation(Entity victim) {
        return new DamageSource(
                victim.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(CALCULATION),
                null,
                null,
                null
        );
    }

    public static DamageSource migraine(Entity victim, EntityWatchtower cause) {
        return new DamageSource(
                victim.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(MIGRAINE),
                null,
                cause,
                null
        );
    }

    public static DamageSource gun(Entity target, LivingEntity shooter) {
        return new DamageSource(
                target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(GUN),
                null,
                shooter,
                shooter.position()
        );
    }
}
