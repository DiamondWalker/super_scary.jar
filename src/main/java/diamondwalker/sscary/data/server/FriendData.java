package diamondwalker.sscary.data.server;

import net.minecraft.nbt.CompoundTag;

public class FriendData extends PersistentWorldData {
    public boolean friendJoined = false;
    public boolean friendDislikesYou = false;
    public boolean friendLeft = false;

    FriendData(WorldData data) {
        super(data);
    }

    @Override
    public String getId() {
        return "friend";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putBoolean("joined", friendJoined);
        tag.putBoolean("dislikes", friendDislikesYou);
        tag.putBoolean("left", friendLeft);
    }

    @Override
    public void load(CompoundTag tag) {
        friendJoined = tag.getBoolean("joined");
        friendDislikesYou = tag.getBoolean("dislikes");
        friendLeft = tag.getBoolean("left");
    }
}
