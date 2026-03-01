package diamondwalker.sscary.script;

import diamondwalker.sscary.util.ChatUtil;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

public abstract class Script {
    private boolean ended = false;
    protected final MinecraftServer server;
    protected final RandomSource random;

    public Script(MinecraftServer server, RandomSource random) {
        this.server = server;
        this.random = random;
    }

    public abstract void tick();

    public void handleChatInput(ServerPlayer sender, String message) {

    }

    public void onStart() {

    }

    public void onEnd() {

    }

    public boolean isCompatibleWith(Script other) {
        return this.getClass() != other.getClass();
    }

    public void sendJoinMessage(String name) {
        chatMessageForAll(ChatUtil.getJoinMessage(name));
    }

    public void sendLeaveMessage(String name) {
        chatMessageForAll(ChatUtil.getLeaveMessage(name));
    }

    public void chatMessageForAll(Component msg) {
        server.getPlayerList().broadcastSystemMessage(msg, false);
    }

    public void popupMessageForAll(Component msg) {
        server.getPlayerList().broadcastSystemMessage(msg, true);
    }

    protected final void end() {
        ended = true;
    }

    public final boolean hasEnded() {
        return ended;
    }

    public void save(CompoundTag nbt) {

    }

    public void load(CompoundTag nbt) {

    }
}
