package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;

public class EnumVariable<T extends Enum<?>> extends ScriptVariable<T, EnumVariable<T>> {
    private final Class<T> theEnum;

    protected EnumVariable(T originalValue, boolean shouldSync, String saveKey, Class<T> theEnum) {
        super(originalValue, shouldSync, saveKey);
        this.theEnum = theEnum;
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

    public static <T extends Enum<?>> ScriptVariable.Builder<T, EnumVariable<T>> create(Class<T> theEnum) {
        return new ScriptVariable.Builder<>(theEnum.getEnumConstants()[0], (value, sync, key) -> new EnumVariable<>(value, sync, key, theEnum));
    }
}
