package diamondwalker.sscary.data.entity.player;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class PlayerSyncHandler implements AttachmentSyncHandler<PlayerData> {
    @Override
    public void write(RegistryFriendlyByteBuf buf, PlayerData attachment, boolean initialSync) {
        // this is sent to all players by default. Maybe I should split the attachment up so it can only be sent to one
        buf.writeBoolean(attachment.eternalPurgatory);
    }

    @Override
    public @Nullable PlayerData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable PlayerData previousValue) {
        if (previousValue == null) previousValue = new PlayerData();

        previousValue.eternalPurgatory = buf.readBoolean();
        return previousValue;
    }
}
