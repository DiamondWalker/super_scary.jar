package diamondwalker.sscary.script.variable;

import java.util.ArrayList;
import java.util.List;

public class ScriptVariableManager {
    private final ArrayList<ScriptVariable<?, ?>> variables = new ArrayList<>();

    protected void add(ScriptVariable<?, ?> variable) {
        for (ScriptVariable<?, ?> var : variables) if (var.saveId.equals(variable.saveId)) throw new IllegalStateException("Conflicting variable save ID: " + var.saveId);
        variables.add(variable);
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
            variables.get(update.id).receive(update);
        }
    }
}
