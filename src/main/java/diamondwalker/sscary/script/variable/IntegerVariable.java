package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;

public class IntegerVariable extends ScriptVariable<Integer, IntegerVariable> {
    protected IntegerVariable(Integer originalValue, boolean shouldSync, String saveKey) {
        super(originalValue, shouldSync, saveKey);
    }

    @Override
    protected void writeToNBT(CompoundTag tag) {
        tag.putInt(saveKey, get());
    }

    @Override
    protected void readFromNBT(CompoundTag tag) {
        set(tag.getInt(saveKey));
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value);
    }

    public static class Update extends ScriptVariable.Update<Integer> {
        private Update(int id, Integer data) {
            super(id, data);
        }

        public Update(ByteBuf buf) {
            super(buf);
        }

        @Override
        public void writeToBuffer(ByteBuf buffer) {
            super.writeToBuffer(buffer);
            buffer.writeInt(data);
        }

        @Override
        public void readFromBuffer(ByteBuf buffer) {
            super.readFromBuffer(buffer);
            data = buffer.readInt();
        }

        @Override
        public ScriptVariableType getType() {
            return SScaryScriptVariables.INTEGER.get();
        }
    }

    public static ScriptVariable.Builder<Integer, IntegerVariable> create() {
        return new ScriptVariable.Builder<>(0, IntegerVariable::new);
    }
}
