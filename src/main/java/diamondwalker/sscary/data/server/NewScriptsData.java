package diamondwalker.sscary.data.server;

import diamondwalker.sscary.network.AddSyncedScriptPacket;
import diamondwalker.sscary.network.RemoveSyncedScriptPacket;
import diamondwalker.sscary.network.UpdateSyncedScriptPacket;
import diamondwalker.sscary.registry.CustomRegistries;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.script.ScriptType;
import diamondwalker.sscary.script.variable.ScriptVariable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class NewScriptsData extends PersistentWorldData {
    private final ArrayList<Script> scripts = new ArrayList<>();

    NewScriptsData(WorldData data) {
        super(data);
    }

    public boolean startScript(Script script) {
        for (Script other : scripts) {
            if (!script.isCompatibleWith(other) || !other.isCompatibleWith(script)) return false;
        }
        scripts.add(script);
        script.onStart();
        if (script.type.shouldSendToClient()) {
            PacketDistributor.sendToAllPlayers(new AddSyncedScriptPacket(
                    CustomRegistries.SCRIPT_REGISTRY.getId(script.type),
                    script.getSyncId(),
                    script.variableManager.getVariablesForSync(false) // send data changed during initialization
            ));
        }
        return true;
    }

    public void tick() {
        int i = 0;
        while (i < scripts.size()) {
            Script script = scripts.get(i);
            if (!script.hasEnded()) script.tick(); // script may have been ended by outside forces, in which case we don't tick
            if (script.hasEnded()) { // script may have ended during the tick, so we need a second check
                script.onEnd();
                scripts.remove(i);
                if (script.type.shouldSendToClient()) {
                    PacketDistributor.sendToAllPlayers(new RemoveSyncedScriptPacket(script.getSyncId()));
                }
            } else {
                List<ScriptVariable.Update<?>> updates = script.variableManager.getVariablesForSync(false);
                if (!updates.isEmpty()) PacketDistributor.sendToAllPlayers(new UpdateSyncedScriptPacket(script.getSyncId(), updates));
                i++;
            }
        }
    }

    public void addNewPlayer(ServerPlayer player) {
        for (Script script : scripts) {
            if (script.type.shouldSendToClient()) {
                PacketDistributor.sendToPlayer(player, new AddSyncedScriptPacket(
                        CustomRegistries.SCRIPT_REGISTRY.getId(script.type),
                        script.getSyncId(),
                        script.variableManager.getVariablesForSync(true) // send all data (because we no longer know which have been changed from default)
                ));
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

    public static final String TYPE_NBT_KEY = "scriptType";
    @Override
    public void save(CompoundTag tag) {
        if (scripts.isEmpty()) return;

        ListTag listTag = new ListTag();
        for (Script script : scripts) {
            CompoundTag compound = new CompoundTag();
            compound.putInt(TYPE_NBT_KEY, CustomRegistries.SCRIPT_REGISTRY.getId(script.type));
            script.variableManager.writeToNBT(compound);
            listTag.add(compound);
        }
        tag.put("scripts", listTag);
    }

    @Override
    public void load(CompoundTag tag) {
        if (!tag.contains("scripts")) return;

        ListTag listTag = tag.getList("scripts", ListTag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compound = listTag.getCompound(i);
            ScriptType<?> type = CustomRegistries.SCRIPT_REGISTRY.byIdOrThrow(compound.getInt(TYPE_NBT_KEY));
            Script script = type.buildForServer(mainData.server);
            script.variableManager.readFromNBT(compound);
            scripts.add(script);
            if (script.type.shouldSendToClient()) {
                PacketDistributor.sendToAllPlayers(new AddSyncedScriptPacket(
                        CustomRegistries.SCRIPT_REGISTRY.getId(script.type),
                        script.getSyncId(),
                        script.variableManager.getVariablesForSync(false) // send data changed during initialization
                ));
            }
        }
    }
}
