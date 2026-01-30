package diamondwalker.sscary.data.server;

import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class ScriptData {
    private HashSet<String> locks = new HashSet<>();
    private ArrayList<ScriptBuilder.ScriptNode> activeScripts = new ArrayList<>();

    public void addScript(ScriptBuilder.ScriptNode event) {
        activeScripts.add(event);
    }

    /**
     * Adds an event lock so other related events can't play at the same time.
     * @param lock The name of the lock
     */
    public void lock(String lock) {
        locks.add(lock);
    }

    /**
     * Checks if the scheduled events currently have this lock
     * @param lock The name of the lock
     * @return True if this is locked, false otherwise
     */
    public boolean hasLock(String lock) {
        return locks.contains(lock);
    }

    /**
     * Unlocks an event sequence lock
     * @param lock The lock to remove
     */
    public void unlock(String lock) {
        locks.remove(lock);
    }

    public ArrayList<Consumer<MinecraftServer>> tickAndGetRemovedScripts() {
        ArrayList<Consumer<MinecraftServer>> list = new ArrayList<>();

        int i = 0;
        while (i < activeScripts.size()) {
            ScriptBuilder.ScriptNode event = activeScripts.get(i);
            if (event.ticks <= 0) {
                list.add(event.action);
                activeScripts.remove(i);
            } else {
                event.ticks--;
                i++;
            }
        }

        return list;
    }
}
