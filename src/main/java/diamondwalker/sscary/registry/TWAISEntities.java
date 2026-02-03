package diamondwalker.sscary.registry;

import diamondwalker.sscary.TWAIS;
import diamondwalker.sscary.entity.bizarrodude.BizarroDudeRenderer;
import diamondwalker.sscary.entity.bizarrodude.EntityBizarroDude;
import diamondwalker.sscary.entity.corrupted.CorruptedRenderer;
import diamondwalker.sscary.entity.corrupted.EntityCorrupted;
import diamondwalker.sscary.entity.nametag.EntityNametag;
import diamondwalker.sscary.entity.nametag.NametagRenderer;
import diamondwalker.sscary.entity.taker.EntityTaker;
import diamondwalker.sscary.entity.taker.ModelTaker;
import diamondwalker.sscary.entity.taker.TakerRenderer;
import diamondwalker.sscary.entity.visage.EntityVisage;
import diamondwalker.sscary.entity.visage.VisageRenderer;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber
public class TWAISEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TWAIS.MODID);

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

    public static final Supplier<EntityType<EntityTaker>> TAKER =  ENTITY_TYPES.register("taker", () -> EntityType.Builder.of(
            EntityTaker::new,
            MobCategory.CREATURE
            )
            .build("taker")
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
        event.put(TWAISEntities.CORRUPTED.get(), EntityCorrupted.createAttributes().build());
        event.put(TWAISEntities.BIZZARO_DUDE.get(), EntityBizarroDude.createAttributes().build());
        event.put(TWAISEntities.TAKER.get(), EntityTaker.createAttributes().build());
    }

    @SubscribeEvent
    private static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TWAISEntities.NAMETAG.get(), NametagRenderer::new);
        event.registerEntityRenderer(TWAISEntities.VISAGE.get(), VisageRenderer::new);
        event.registerEntityRenderer(TWAISEntities.CORRUPTED.get(), (cntx) -> new CorruptedRenderer(cntx, false));
        event.registerEntityRenderer(TWAISEntities.BIZZARO_DUDE.get(), BizarroDudeRenderer::new);
        event.registerEntityRenderer(TWAISEntities.TAKER.get(), TakerRenderer::new);
    }

    @SubscribeEvent
    private static void registerEntityRenders(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelTaker.LAYER_LOCATION, ModelTaker::createBodyLayer);
    }

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
