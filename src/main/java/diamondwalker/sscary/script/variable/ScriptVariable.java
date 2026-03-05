package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.CustomRegistries;
import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public abstract class ScriptVariable<T, E extends ScriptVariable<T, ?>> {
    public static final StreamCodec<ByteBuf, List<Update<?>>> STREAM_CODEC = new StreamCodec<>() {
        public List<Update<?>> decode(ByteBuf buf) {
            List<Update<?>> list = new ArrayList<>();
            int size = buf.readInt();

            for (int i = 0; i < size; i++) {
                int type = buf.readInt();
                list.add(CustomRegistries.SCRIPT_VARIABLE_TYPE_REGISTRY.byIdOrThrow(type).build(buf));
            }

            return list;
        }

        public void encode(ByteBuf buf, List<Update<?>> list) {
            buf.writeInt(list.size());
            for (Update<?> var : list) {
                buf.writeInt(CustomRegistries.SCRIPT_VARIABLE_TYPE_REGISTRY.getId(var.getType()));
                var.writeToBuffer(buf);
            }
        }
    };

    protected T value;
    protected T syncedValue; // the last value that was synced to the client

    protected boolean shouldSync = false;
    protected String saveKey;

    protected ScriptVariable(T originalValue) {
        this.value = this.syncedValue = originalValue;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    /**
     * Called on the client side to read a new value from an update.
     * Override if updates need custom handling (e.g. enum variables being read from integer updates)
     */
    protected void receive(Update<?> update) {
        value = (T)update.data;
    }

    public boolean isChanged() {
        return !syncedValue.equals(value);
    }

    public void markSynced() {
        syncedValue = value;
    }

    public E sync() {
        shouldSync = true;
        return (E)this;
    }

    public E save(String id) {
        saveKey = id;
        return (E)this;
    }

    protected abstract void writeToNBT(CompoundTag tag);

    protected abstract void readFromNBT(CompoundTag tag);

    public abstract Update<?> getUpdate(int id);

    /**
     * This class represents a data change that will be transmitted through the script update packet
     * @param <T> The type of the data being sent. Usually the same as the ScriptVariable's contained type, except in instances where the ScriptVariable transmits data a different way (e.g. enums)
     */
    public static abstract class Update<T> {
        protected int id;
        protected T data;

        protected Update(int id, T data) {
            this.id = id;
            this.data = data;
        }

        protected Update(ByteBuf buf) {
            this.readFromBuffer(buf);
        }

        public void writeToBuffer(ByteBuf buffer) {
            buffer.writeInt(id);
        }

        public void readFromBuffer(ByteBuf buffer) {
            id = buffer.readInt();
        }

        public abstract ScriptVariableType getType();
    }
}
