package diamondwalker.sscary.data.client;

import com.google.common.collect.ImmutableList;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.CustomRegistries;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.script.ScriptType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NewScriptsClientData {
    private final ArrayList<Script> scripts = new ArrayList<>();
    private final HashMap<Integer, Script> syncedScriptMap = new HashMap<>();

    public void addScript(Script script) {
        scripts.add(script);
        if (syncedScriptMap.put(script.getSyncId(), script) != null) throw new IllegalStateException("A script with id " + script.getSyncId() + " was already on the client!");
        script.onStart();
    }

    public Script getScriptFromSyncId(int id) {
        return syncedScriptMap.get(id);
    }

    public void tick() {
        int i = 0;
        while (i < scripts.size()) {
            Script script = scripts.get(i);
            if (!script.hasEnded()) script.tick(); // script may have been ended by outside forces, in which case we don't tick
            if (script.hasEnded()) { // script may have ended during the tick, so we need a second check
                script.onEnd();
                if (syncedScriptMap.remove(script.getSyncId()) == null) throw new IllegalStateException("Script with id " + script.getSyncId() + " was not on the client!");
                scripts.remove(script);
            } else {
                i++;
            }
        }
    }

    public List<Script> getScripts() {
        return scripts.stream().toList();
    }
}
