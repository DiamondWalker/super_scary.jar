package diamondwalker.sscary.data.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.UUID;

public class PlayerKillerData extends PersistentWorldData {
    private HashSet<UUID> threatenedPlayers = new HashSet<>();

    PlayerKillerData(WorldData data) {
        super(data);
    }

    public void addThreatenedPlayer(ServerPlayer player) {
        threatenedPlayers.add(player.getGameProfile().getId());
    }

    public boolean hasPlayerBeenThreatened(ServerPlayer player) {
        return threatenedPlayers.contains(player.getGameProfile().getId());
    }

    @Override
    public String getId() {
        return "playerKiller";
    }

    @Override
    public void save(CompoundTag tag) {
        ListTag listTag = new ListTag();
        for (UUID id : threatenedPlayers) {
            listTag.add(NbtUtils.createUUID(id));
        }
        tag.put("threatenedPlayers", listTag);
    }

    @Override
    public void load(CompoundTag tag) {
        ListTag list = tag.getList("threatenedPlayers", ListTag.TAG_INT_ARRAY);
        for (Tag value : list) {
            threatenedPlayers.add(NbtUtils.loadUUID(value));
        }
    }
}
