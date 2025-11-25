package diamondwalker.twais.registry;

import diamondwalker.twais.TWAIS;
import diamondwalker.twais.data.entity.player.PlayerData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class TWAISDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TWAIS.MODID);

    public static final Supplier<AttachmentType<PlayerData>> PLAYER = ATTACHMENT_TYPES.register(
            "player", () -> AttachmentType.serializable(PlayerData::new).build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
