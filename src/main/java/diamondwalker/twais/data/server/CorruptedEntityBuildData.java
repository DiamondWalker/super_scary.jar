package diamondwalker.twais.data.server;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;

public class CorruptedEntityBuildData extends PersistentWorldData {
    private ArrayList<BlockPos> builds = new ArrayList<>();
    public int anger = 0;

    CorruptedEntityBuildData(WorldData data) {
        super(data);
    }

    public void addBuild(BlockPos pos) {
        if (builds != null) builds.add(pos);
    }

    public boolean isBuildAt(BlockPos pos) {
        if (builds == null) return false;
        for (BlockPos buildPos : builds) {
            if (
                    Math.abs(pos.getX() - buildPos.getX()) <= 12 &&
                    Math.abs(pos.getZ() - buildPos.getZ()) <= 12 &&
                    pos.getY() >= buildPos.getY() && pos.getY() - buildPos.getY() <= 12
            )  return true;
        }

        return false;
    }

    public void flush() {
        builds = null;
    }

    @Override
    public String getId() {
        return "corrupted_builds";
    }

    @Override
    public void save(CompoundTag tag) {
        if (builds != null) {
            int count = builds.size();

            ListTag listTag = new ListTag();
            for (int i = 0; i < count; i++) {
                BlockPos pos = builds.get(i);
                listTag.add(IntTag.valueOf(pos.getX()));
                listTag.add(IntTag.valueOf(pos.getY()));
                listTag.add(IntTag.valueOf(pos.getZ()));
            }

            tag.put("builds", listTag);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if (!tag.contains("builds")) {
            flush();
        } else {
            ListTag list = tag.getList("builds", ListTag.TAG_INT);

            for (int i = 0; i < list.size(); i += 3) {
                builds.add(new BlockPos(list.getInt(i + 0), list.getInt(i + 1), list.getInt(i + 2)));
            }
        }
    }
}
