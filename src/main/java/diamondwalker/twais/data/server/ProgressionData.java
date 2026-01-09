package diamondwalker.twais.data.server;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class ProgressionData extends PersistentWorldData {
    private long time = 0;
    private long timeAngered = -1;

    private boolean bug = false;

    ProgressionData(WorldData data) {
        super(data);
    }

    public void bug() {
        bug = true;
    }

    public boolean hasSeenBug() {
        return bug;
    }

    public void incrementTime() {
        time++;
    }

    public long getTimeInWorld() {
        return time;
    }

    public void startAnger(MinecraftServer server) {
        timeAngered = time;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 0));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 5));
        }
        server.overworld().globalLevelEvent(1023, BlockPos.ZERO, 0);
        mainData.eventCooldown();
    }

    public boolean hasBeenAngered() {
        return timeAngered >= 0;
    }

    public long timeSinceAngered() {
        if (timeAngered < 0) return time;
        return time - timeAngered;
    }

    @Override
    public String getId() {
        return "progression";
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putLong("time", time);
        tag.putLong("angered", timeAngered);
        tag.putBoolean("bug", bug);
    }

    @Override
    public void load(CompoundTag tag) {
        time = tag.getLong("time");
        timeAngered = tag.getLong("angered");
        bug = tag.getBoolean("bug");
    }
}
