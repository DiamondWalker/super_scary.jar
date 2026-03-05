package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;

public class EnumVariable<T extends Enum<?>> extends ScriptVariable<T, EnumVariable<T>> {
    private final Class<T> theEnum;

    public EnumVariable(T originalValue) {
        super(originalValue);
        theEnum = (Class<T>) originalValue.getClass();
    }

    @Override
    protected void writeToNBT(CompoundTag tag) {
        tag.putInt(saveKey, get().ordinal());
    }

    @Override
    protected void readFromNBT(CompoundTag tag) {
        set(theEnum.getEnumConstants()[tag.getInt(saveKey)]);
    }

    // bit of a hack; we're using the integer updates and then converting
    @Override
    protected void receive(ScriptVariable.Update<?> update) {
        value = theEnum.getEnumConstants()[((Update)update).data];
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value.ordinal());
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
            return SScaryScriptVariables.ENUM.get();
        }
    }
}
