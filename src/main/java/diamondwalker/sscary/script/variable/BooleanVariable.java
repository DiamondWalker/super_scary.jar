package diamondwalker.sscary.script.variable;

import io.netty.buffer.ByteBuf;

public class BooleanVariable extends ScriptVariable<Boolean> {
    public BooleanVariable(ScriptVariableManager manager, boolean originalValue) {
        super(manager, originalValue);
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value);
    }

    protected static class Update extends ScriptVariable.Update<Boolean> {
        protected Update(int id, Boolean data) {
            super(id, data);
        }

        protected Update(ByteBuf buf) {
            super(buf);
        }

        @Override
        public void writeToBuffer(ByteBuf buffer) {
            buffer.writeBoolean(data);
        }

        @Override
        public void readFromBuffer(ByteBuf buffer) {
            data = buffer.readBoolean();
        }

        @Override
        public VariableType getType() {
            return VariableType.BOOL;
        }
    }
}
