package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;

public class BooleanVariable extends ScriptVariable<Boolean, BooleanVariable> {
    protected BooleanVariable(Boolean originalValue, boolean shouldSync, String saveKey) {
        super(originalValue, shouldSync, saveKey);
    }

    @Override
    protected void writeToNBT(CompoundTag tag) {
        tag.putBoolean(saveKey, get());
    }

    @Override
    protected void readFromNBT(CompoundTag tag) {
        set(tag.getBoolean(saveKey));
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value);
    }

    public static class Update extends ScriptVariable.Update<Boolean> {
        protected Update(int id, Boolean data) {
            super(id, data);
        }

        public Update(ByteBuf buf) {
            super(buf);
        }

        @Override
        public void writeToBuffer(ByteBuf buffer) {
            super.writeToBuffer(buffer);
            buffer.writeBoolean(data);
        }

        @Override
        public void readFromBuffer(ByteBuf buffer) {
            super.readFromBuffer(buffer);
            data = buffer.readBoolean();
        }

        @Override
        public ScriptVariableType getType() {
            return SScaryScriptVariables.BOOLEAN.get();
        }
    }

    public static ScriptVariable.Builder<Boolean, BooleanVariable> create() {
        return new ScriptVariable.Builder<>(false, BooleanVariable::new);
    }
}
