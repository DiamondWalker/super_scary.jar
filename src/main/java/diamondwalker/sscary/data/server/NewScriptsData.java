package diamondwalker.sscary.data.server;

import diamondwalker.sscary.script.Script;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;

public class NewScriptsData extends PersistentWorldData {
    private ArrayList<Script> scripts = new ArrayList<>();

    NewScriptsData(WorldData data) {
        super(data);
    }

    public boolean startScript(Script script) {
        for (Script other : scripts) {
            if (!script.isCompatibleWith(other) || !other.isCompatibleWith(script)) return false;
        }
        scripts.add(script);
        script.onStart();
        return true;
    }

    public void tick() {
        int i = 0;
        while (i < scripts.size()) {
            Script script = scripts.get(i);
            script.tick();
            if (script.hasEnded()) {
                script.onEnd();
                scripts.remove(i);
            } else {
                i++;
            }
        }
    }

    public void handleChat(ServerPlayer sender, String message) {
        for (Script script : scripts) script.handleChatInput(sender, message);
    }

    @Override
    public String getId() {
        return "scripts";
    }

    // TODO: saving isn't done yet. I need to figure out how to register the scripts
    @Override
    public void save(CompoundTag tag) {
        /*if (scripts.isEmpty()) return;

        ListTag listTag = new ListTag();
        for (Script script : scripts) {
            CompoundTag compound = new CompoundTag();
            script.save(compound);
            listTag.add(compound);
        }
        tag.put("scripts", listTag);*/
    }

    @Override
    public void load(CompoundTag tag) {
        /*if (!tag.contains("scripts")) return;

        ListTag listTag = tag.getList("scripts", ListTag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compound = listTag.getCompound(i);
        }*/
    }
}
