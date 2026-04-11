package diamondwalker.sscary.data.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashSet;

public class ChunkData implements INBTSerializable<ListTag> {
    public HashSet<BlockPos> baseTiles = new HashSet<>();

    @Override
    public ListTag serializeNBT(HolderLookup.Provider provider) {
        ListTag listTag = new ListTag();
        for (BlockPos pos : baseTiles) {
            listTag.add(IntTag.valueOf(pos.getX()));
            listTag.add(IntTag.valueOf(pos.getY()));
            listTag.add(IntTag.valueOf(pos.getZ()));
        }
        return listTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, ListTag listTag) {
        if (listTag.size() % 3 != 0) throw new IllegalStateException("List tag should be multiple of 3!");
        for (int i = 0; i < listTag.size() / 3; i++) {
            int index = i * 3;
            baseTiles.add(
                    new BlockPos(
                            listTag.getInt(index + 0),
                            listTag.getInt(index + 1),
                            listTag.getInt(index + 2)
                    )
            );
        }
    }
}
