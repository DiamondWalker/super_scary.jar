package diamondwalker.sscary.script.variable;

import io.netty.buffer.ByteBuf;

public class IntegerVariable extends ScriptVariable<Integer, IntegerVariable> {
    public IntegerVariable(ScriptVariableManager manager, int originalValue) {
        super(manager, originalValue);
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value);
    }

    protected static class Update extends ScriptVariable.Update<Integer> {
        private Update(int id, Integer data) {
            super(id, data);
        }

        protected Update(ByteBuf buf) {
            super(buf);
        }

        @Override
        public void writeToBuffer(ByteBuf buffer) {
            buffer.writeInt(data);
        }

        @Override
        public void readFromBuffer(ByteBuf buffer) {
            buffer.writeInt(data);
        }

        @Override
        public VariableType getType() {
            return VariableType.INT;
        }
    }
}
