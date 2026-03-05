package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.script.Script;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class ScriptVariableManager {
    private final ArrayList<ScriptVariable<?, ?>> variables = new ArrayList<>();
    private final Script script;

    public ScriptVariableManager(Script script) {
        this.script = script;
    }

    public <T extends ScriptVariable<?, ?>> T add(T variable) {
        for (ScriptVariable<?, ?> var : variables) if (var.saveKey.equals(variable.saveKey)) throw new IllegalStateException("Conflicting variable save ID: " + var.saveKey);
        variables.add(variable);
        return variable;
    }

    public List<ScriptVariable.Update<?>> getVariablesForSync() {
        List<ScriptVariable.Update<?>> list = new ArrayList<>();

        for (int i = 0; i < variables.size(); i++) {
            ScriptVariable<?, ?> variable = variables.get(i);
            if (variable.shouldSync && variable.isChanged()) {
                list.add(variable.getUpdate(i));
                variable.markSynced();
            }
        }

        return list;
    }

    public void receiveUpdates(List<ScriptVariable.Update<?>> updates) {
        for (ScriptVariable.Update<?> update : updates) {
            ScriptVariable<?, ?> variable = variables.get(update.id);
            variable.receive(update);
            script.onVariableUpdate(variable);
            variable.markSynced();
        }
    }

    public void writeToNBT(CompoundTag nbt) {
        for (ScriptVariable<?, ?> var : variables) {
            if (var.saveKey != null) var.writeToNBT(nbt);
        }
    }

    public void readFromNBT(CompoundTag nbt) {
        for (ScriptVariable<?, ?> var : variables) {
            if (var.saveKey != null) var.readFromNBT(nbt);
        }
    }
}
