package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Utf8String;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationVariable extends ScriptVariable<ResourceLocation, ScriptVariable<ResourceLocation, ?>> {
    protected ResourceLocationVariable(ResourceLocation originalValue, boolean shouldSync, String saveKey) {
        super(originalValue, shouldSync, saveKey);
    }

    @Override
    protected void writeToNBT(CompoundTag tag) {
        tag.putString(saveKey, get().toString());
    }

    @Override
    protected void readFromNBT(CompoundTag tag) {
        set(ResourceLocation.parse(tag.getString(saveKey)));
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value);
    }

    public static class Update extends ScriptVariable.Update<ResourceLocation> {
        private Update(int id, ResourceLocation data) {
            super(id, data);
        }

        public Update(ByteBuf buf) {
            super(buf);
        }

        @Override
        public void writeToBuffer(ByteBuf buffer) {
            super.writeToBuffer(buffer);
            Utf8String.write(buffer, data.toString(), 32767);
        }

        @Override
        public void readFromBuffer(ByteBuf buffer) {
            super.readFromBuffer(buffer);
            data = ResourceLocation.parse(Utf8String.read(buffer, 32767));
        }

        @Override
        public ScriptVariableType getType() {
            return SScaryScriptVariables.RESOURCE_LOCATION.get();
        }
    }

    public static ScriptVariable.Builder<ResourceLocation, ResourceLocationVariable> create() {
        return new ScriptVariable.Builder<>(null, ResourceLocationVariable::new);
    }
}
