package diamondwalker.sscary.registry;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.chunk.ChunkData;
import diamondwalker.sscary.data.entity.player.PlayerData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class SScaryDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, SScary.MODID);

    public static final Supplier<AttachmentType<PlayerData>> PLAYER = ATTACHMENT_TYPES.register(
            "player", () -> AttachmentType.serializable(PlayerData::new)
                    .copyOnDeath()
                    .copyHandler(PlayerData::copyToNewPlayer)
                    .build()
    );

    public static final Supplier<AttachmentType<ChunkData>> CHUNK = ATTACHMENT_TYPES.register(
            "chunk", () -> AttachmentType.serializable(ChunkData::new)
                    .build()
    );

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
