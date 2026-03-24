package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.entity.entity.bizarrodude.BizarroDudeRenderer;
import diamondwalker.sscary.entity.entity.bizarrodude.EntityBizarroDude;
import diamondwalker.sscary.entity.entity.construct.ConstructRenderer;
import diamondwalker.sscary.entity.entity.construct.EntityConstruct;
import diamondwalker.sscary.entity.entity.corrupted.CorruptedRenderer;
import diamondwalker.sscary.entity.entity.corrupted.EntityCorrupted;
import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
import diamondwalker.sscary.entity.entity.friedsteve.FriedSteveRenderer;
import diamondwalker.sscary.entity.entity.unauthorized.EntityUnauthorized;
import diamondwalker.sscary.entity.entity.unauthorized.UnauthorizedRenderer;
import diamondwalker.sscary.entity.entity.watchtower.EntityWatchtower;
import diamondwalker.sscary.entity.entity.watchtower.ModelWatchtower;
import diamondwalker.sscary.entity.entity.watchtower.WatchtowerRenderer;
import diamondwalker.sscary.entity.misc.nametag.EntityNametag;
import diamondwalker.sscary.entity.entity.taker.EntityTaker;
import diamondwalker.sscary.entity.entity.taker.ModelTaker;
import diamondwalker.sscary.entity.entity.taker.TakerRenderer;
import diamondwalker.sscary.entity.entity.visage.EntityVisage;
import diamondwalker.sscary.entity.entity.visage.VisageRenderer;
import diamondwalker.sscary.entity.projectile.pepperspray.EntityPepperSpray;
import diamondwalker.sscary.mobspawner.SurfaceEntitySpawner;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.level.ModifyCustomSpawnersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber
public class SScaryEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SScary.MODID);

    public static final Supplier<EntityType<EntityNametag>> NAMETAG = ENTITY_TYPES.register("name_tag", () -> EntityType.Builder.of(
            EntityNametag::new,
            MobCategory.MISC
            )
            .fireImmune()
            .sized(0.0f, 0.0f)
            .build("name_tag")
    );

    public static final Supplier<EntityType<EntityVisage>> VISAGE = ENTITY_TYPES.register("visage", () -> EntityType.Builder.of(
            EntityVisage::new,
            MobCategory.CREATURE
            )
            .sized(1.0f, 1.0f)
            .eyeHeight(0.5f)
            .fireImmune()
            .build("visage")
    );

    public static final Supplier<EntityType<EntityCorrupted>> CORRUPTED = ENTITY_TYPES.register("corrupted", () -> EntityType.Builder.of(
            EntityCorrupted::new,
            MobCategory.CREATURE
            )
            .sized(0.6F, 1.8F)
            .eyeHeight(1.62F)
            .clientTrackingRange(300)
            .noSave()
            .build("corrupted")
    );

    public static final Supplier<EntityType<EntityFriedSteve>> FRIED_STEVE = ENTITY_TYPES.register("fried_steve", () -> EntityType.Builder.of(
            EntityFriedSteve::new,
            MobCategory.CREATURE
            )
            .sized(0.6f, 1.8f)
            .eyeHeight(1.62f)
            .build("fried_steve")
    );

    public static final Supplier<EntityType<EntityTaker>> TAKER = ENTITY_TYPES.register("taker", () -> EntityType.Builder.of(
            EntityTaker::new,
            MobCategory.CREATURE
            )
            .build("taker")
    );

    public static final Supplier<EntityType<EntityWatchtower>> WATCHTOWER = ENTITY_TYPES.register("watchtower", () -> EntityType.Builder.of(
            EntityWatchtower::new,
            MobCategory.CREATURE
            )
            .sized(1.5f, 37.5f)
            .eyeHeight(47.3f)
            .clientTrackingRange(300)
            .build("watchtower")
    );

    public static final Supplier<EntityType<EntityConstruct>> CONSTRUCT = ENTITY_TYPES.register("construct", () -> EntityType.Builder.of(
                            EntityConstruct::new,
                            MobCategory.CREATURE
                    )
                    .sized(0.6f, 1.8f)
                    .eyeHeight(1.62f)
                    .build("construct")
    );

    public static final Supplier<EntityType<EntityUnauthorized>> UNAUTHORIZED = ENTITY_TYPES.register("unauthorized", () -> EntityType.Builder.of(
            EntityUnauthorized::new,
            MobCategory.CREATURE
    )
            .sized(1.0f, 3.0f)
            .eyeHeight(2.75f)
            .build("unauthorized"));

    public static final Supplier<EntityType<EntityPepperSpray>> PEPPER_SPRAY = ENTITY_TYPES.register("pepper_spray", () -> EntityType.Builder.of(
                (EntityType<EntityPepperSpray> type, Level level) -> new EntityPepperSpray(type, level), MobCategory.MISC
            )
            .sized(0.2f, 0.2f)
            .build("pepper_spray")
    );

    public static final Supplier<EntityType<EntityBizarroDude>> BIZZARO_DUDE = ENTITY_TYPES.register("bizarro", () -> EntityType.Builder.of(
                            EntityBizarroDude::new,
                            MobCategory.CREATURE
                    )
                    .sized(0.6F, 1.8F)
                    .eyeHeight(0.18F)
                    .clientTrackingRange(300)
                    .noSave()
                    .build("bizarro")
    );

    @SubscribeEvent
    private static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(SScaryEntities.CORRUPTED.get(), EntityCorrupted.createAttributes().build());
        event.put(SScaryEntities.BIZZARO_DUDE.get(), EntityBizarroDude.createAttributes().build());
        event.put(SScaryEntities.TAKER.get(), EntityTaker.createAttributes().build());
        event.put(SScaryEntities.FRIED_STEVE.get(), EntityFriedSteve.createAttributes().build());
        event.put(SScaryEntities.WATCHTOWER.get(), EntityWatchtower.createAttributes().build());
        event.put(SScaryEntities.CONSTRUCT.get(), EntityConstruct.createAttributes().build());
        event.put(SScaryEntities.UNAUTHORIZED.get(), EntityUnauthorized.createAttributes().build());
    }

    @SubscribeEvent
    private static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SScaryEntities.NAMETAG.get(), NoopRenderer<EntityNametag>::new);
        event.registerEntityRenderer(SScaryEntities.VISAGE.get(), VisageRenderer::new);
        event.registerEntityRenderer(SScaryEntities.CORRUPTED.get(), (cntx) -> new CorruptedRenderer(cntx, false));
        event.registerEntityRenderer(SScaryEntities.BIZZARO_DUDE.get(), BizarroDudeRenderer::new);
        event.registerEntityRenderer(SScaryEntities.TAKER.get(), TakerRenderer::new);
        event.registerEntityRenderer(SScaryEntities.WATCHTOWER.get(), WatchtowerRenderer::new);
        event.registerEntityRenderer(SScaryEntities.FRIED_STEVE.get(), FriedSteveRenderer::new);
        event.registerEntityRenderer(SScaryEntities.CONSTRUCT.get(), ConstructRenderer::new);
        event.registerEntityRenderer(SScaryEntities.UNAUTHORIZED.get(), UnauthorizedRenderer::new);

        event.registerEntityRenderer(SScaryEntities.PEPPER_SPRAY.get(), NoopRenderer<EntityPepperSpray>::new);
    }

    @SubscribeEvent
    private static void registerEntityRenders(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelTaker.LAYER_LOCATION, ModelTaker::createBodyLayer);
        event.registerLayerDefinition(ModelWatchtower.LAYER_LOCATION, ModelWatchtower::createBodyLayer);
    }

    @SubscribeEvent
    private static void registerEntitySpawners(ModifyCustomSpawnersEvent event) {
        if (event.getLevel().dimension() == Level.OVERWORLD) {
            event.addCustomSpawner(new SurfaceEntitySpawner(SScaryEntities.WATCHTOWER.get(), 18000, false, true, 60, 100));
            event.addCustomSpawner(new SurfaceEntitySpawner(SScaryEntities.CONSTRUCT.get(), 8000, false, true, 40, 80));

        }
    }

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
