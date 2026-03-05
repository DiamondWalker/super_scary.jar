package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Utf8String;

public class StringVariable extends ScriptVariable<String, StringVariable> {
    public StringVariable(String originalValue) {
        super(originalValue);
    }

    @Override
    protected void writeToNBT(CompoundTag tag) {
        tag.putString(saveKey, get());
    }

    @Override
    protected void readFromNBT(CompoundTag tag) {
        set(tag.getString(saveKey));
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value);
    }

    public static class Update extends ScriptVariable.Update<String> {
        private Update(int id, String data) {
            super(id, data);
        }

        public Update(ByteBuf buf) {
            super(buf);
        }

        @Override
        public void writeToBuffer(ByteBuf buffer) {
            super.writeToBuffer(buffer);
            Utf8String.write(buffer, data, 32767);
        }

        @Override
        public void readFromBuffer(ByteBuf buffer) {
            super.readFromBuffer(buffer);
            data = Utf8String.read(buffer, 32767);
        }

        @Override
        public ScriptVariableType getType() {
            return SScaryScriptVariables.STRING.get();
        }
    }
}
