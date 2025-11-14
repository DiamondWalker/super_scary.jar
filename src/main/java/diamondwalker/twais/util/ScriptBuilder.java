package diamondwalker.twais.util;

import diamondwalker.twais.data.server.ScriptData;
import diamondwalker.twais.data.server.WorldData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.util.Stack;
import java.util.function.Consumer;

public class ScriptBuilder {
    private final ScriptData scriptData;
    private long ticks;

    private final String[] locks;

    private final Stack<ScriptNode> nodes = new Stack<>();

    public ScriptBuilder(MinecraftServer arg) {
       this(arg, (String) null);
    }

    public ScriptBuilder(WorldData data) {
        this(data, (String) null);
    }

    public ScriptBuilder(MinecraftServer arg, String... locks) {
        this(WorldData.get(arg), locks);
    }

    public ScriptBuilder(WorldData data, String... locks) {
        scriptData = data.scripts;

        this.locks = locks != null ? locks : new String[] {};
    }

    public final ScriptBuilder rest(long ticks) {
        if (ticks > 0) {
            this.ticks += ticks;
            return this;
        }

        throw new IllegalArgumentException("Cannot rest for " + ticks + " ticks!");
    }

    public ScriptBuilder action(Consumer<MinecraftServer> action) {
        nodes.add(new ScriptNode(ticks, action));
        return this;
    }

    public ScriptBuilder chatMessageForAll(Component msg) {
        return action((server) -> server.getPlayerList().broadcastSystemMessage(msg, false));
    }

    public ScriptBuilder popupMessageForAll(Component msg) {
        return action((server) -> server.getPlayerList().broadcastSystemMessage(msg, true));
    }

    public boolean startScript() {
        // if any of the locks are currently active, we return
        for (String lock : locks) {
            if (scriptData.hasLock(lock)) return false;
        }

        // add the locks
        for (String lock : locks) scriptData.lock(lock);

        // we will schedule it so it unlocks at the end of the sequence
        action((server) -> {
            for (String lock : locks) WorldData.get(server).scripts.unlock(lock);
        });

        // now we actually add all the scheduled actions
        while (!nodes.empty()) scriptData.addScript(nodes.pop());
        return true;
    }


    public static class ScriptNode {
        public long ticks;
        public final Consumer<MinecraftServer> action;

        private ScriptNode(long ticks, Consumer<MinecraftServer> action) {
            this.ticks = ticks;
            this.action = action;
        }
    }
}
