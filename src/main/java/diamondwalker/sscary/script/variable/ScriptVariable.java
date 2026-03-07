package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.CustomRegistries;
import diamondwalker.sscary.registry.SScaryScriptVariables;
import diamondwalker.sscary.script.Script;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.lang3.function.TriFunction;

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

    protected final boolean shouldSync;
    protected final String saveKey;

    protected ScriptVariable(T originalValue, boolean shouldSync, String saveKey) {
        this.value = this.syncedValue = originalValue;
        this.shouldSync = shouldSync;
        this.saveKey = saveKey;
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

    public static final class Builder<T, E extends ScriptVariable<T, ?>> {
        protected T startingValue;
        protected boolean sync;
        protected String save;

        private final TriFunction<T, Boolean, String, E> constructor;

        protected Builder(T defaultValue, TriFunction<T, Boolean, String, E> constructor) {
            this.constructor = constructor;
            this.startingValue = defaultValue;
        }

        public E define(Script script) {
            E variable = constructor.apply(startingValue, sync, save);
            script.variableManager.add(variable);
            return variable;
        }

        public void defaultValue(T value) {
            startingValue = value;
        }

        public Builder<T, E> sync() {
            sync = true;
            return this;
        }

        public Builder<T, E> save(String saveKey) {
            save = saveKey;
            return this;
        }
    }
}
