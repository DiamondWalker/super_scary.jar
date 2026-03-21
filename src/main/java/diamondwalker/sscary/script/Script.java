package diamondwalker.sscary.script;

import diamondwalker.sscary.script.variable.ScriptVariable;
import diamondwalker.sscary.script.variable.ScriptVariableManager;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public abstract class Script {
    private boolean ended = false;

    public final ScriptType<? extends Script> type;

    protected final MinecraftServer server;
    protected final RandomSource random = RandomSource.create();
    protected final boolean clientSide;

    public final ScriptVariableManager variableManager = new ScriptVariableManager(this);

    private static int currentSyncId;
    private final int syncId;

    public Script(ScriptType<? extends Script> type, MinecraftServer server) {
        this.type = type;
        this.server = server;
        clientSide = false;

        if (type.shouldSendToClient()) {
            syncId = currentSyncId++;
        } else {
            syncId = 0;
        }
    }

    public Script(ScriptType<? extends Script> type, int clientId) {
        this.type = type;
        this.server = null;
        clientSide = true;
        syncId = clientId;
    }

    public abstract void tick();

    public void handleChatInput(ServerPlayer sender, String message) {

    }

    public void handleBlockBreak(ServerPlayer breaker, BlockState state, BlockPos blockPos) {

    }

    public void modifyLightmap(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {}

    public Music getMusic() {
        return null;
    }

    public void onStart() {

    }

    public void onEnd() {

    }

    public final int getSyncId() {
        if (!type.shouldSendToClient()) throw new IllegalStateException("This script isn't synced to the client so the sync id should be unused!");

        return syncId;
    }

    public void onVariableUpdate(ScriptVariable<?, ?> updatedVariable) {

    }

    public boolean isCompatibleWith(Script other) {
        return this.type != other.type;
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

    public final void end() {
        ended = true;
    }

    public final boolean hasEnded() {
        return ended;
    }
}
