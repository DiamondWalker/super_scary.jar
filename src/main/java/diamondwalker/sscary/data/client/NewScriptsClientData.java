package diamondwalker.sscary.data.client;

import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.CustomRegistries;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.script.ScriptType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class NewScriptsClientData {
    private final ArrayList<Script> scripts = new ArrayList<>();
    private final HashMap<Integer, Script> syncedScriptMap = new HashMap<>();

    public void addScript(Script script) {
        scripts.add(script);
        if (syncedScriptMap.put(script.getSyncId(), script) != null) throw new IllegalStateException("A script with id " + script.getSyncId() + " was already on the client!");
        script.onStart();
    }

    public void removeScript(Script script) {
        script.onEnd();
        if (syncedScriptMap.remove(script.getSyncId()) == null) throw new IllegalStateException("Script with id " + script.getSyncId() + " was not on the client!");
        scripts.remove(script);
    }

    public Script getScriptFromSyncId(int id) {
        return syncedScriptMap.get(id);
    }

    public void tick() {
        int i = 0;
        while (i < scripts.size()) {
            Script script = scripts.get(i);
            script.tick();
            if (script.hasEnded()) {
                removeScript(script);
            } else {
                i++;
            }
        }
    }
}
