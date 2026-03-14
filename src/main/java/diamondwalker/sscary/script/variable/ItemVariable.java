package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Utf8String;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ItemVariable extends ScriptVariable<Item, ScriptVariable<Item, ?>> {
    protected ItemVariable(Item originalValue, boolean shouldSync, String saveKey) {
        super(originalValue, shouldSync, saveKey);
    }

    @Override
    protected void writeToNBT(CompoundTag tag) {
        tag.putString(saveKey, BuiltInRegistries.ITEM.getKey(get()).toString());
    }

    @Override
    protected void readFromNBT(CompoundTag tag) {
        set(BuiltInRegistries.ITEM.get(ResourceLocation.parse(tag.getString(saveKey))));
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value);
    }

    public static class Update extends ScriptVariable.Update<Item> {
        private Update(int id, Item data) {
            super(id, data);
        }

        public Update(ByteBuf buf) {
            super(buf);
        }

        @Override
        public void writeToBuffer(ByteBuf buffer) {
            super.writeToBuffer(buffer);
            buffer.writeInt(BuiltInRegistries.ITEM.getId(data));
        }

        @Override
        public void readFromBuffer(ByteBuf buffer) {
            super.readFromBuffer(buffer);
            data = BuiltInRegistries.ITEM.byId(buffer.readInt());
        }

        @Override
        public ScriptVariableType getType() {
            return SScaryScriptVariables.ITEM.get();
        }
    }

    public static ScriptVariable.Builder<Item, ItemVariable> create() {
        return new ScriptVariable.Builder<>(Items.AIR, ItemVariable::new);
    }
}