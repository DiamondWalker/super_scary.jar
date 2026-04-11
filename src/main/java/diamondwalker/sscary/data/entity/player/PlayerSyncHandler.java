package diamondwalker.sscary.data.entity.player;

import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;

public class PlayerSyncHandler implements IAttachmentCopyHandler<PlayerData> {
    @Override
    public @Nullable PlayerData copy(PlayerData attachment, IAttachmentHolder holder, HolderLookup.Provider provider) {
        return null;
    }
}
