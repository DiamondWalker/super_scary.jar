package diamondwalker.sscary.script.variable;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class ScriptVariable<T> {
    public static final StreamCodec<ByteBuf, List<Update<?>>> STREAM_CODEC = new StreamCodec<>() {
        public List<Update<?>> decode(ByteBuf buf) {
            List<Update<?>> list = new ArrayList<>();
            int size = buf.readInt();

            for (int i = 0; i < size; i++) {
                int type = buf.readInt();
                list.add(VariableType.values()[type].constructor.apply(buf));
            }

            return list;
        }

        public void encode(ByteBuf buf, List<Update<?>> list) {
            buf.writeInt(list.size());
            for (Update<?> var : list) {
                buf.writeInt(var.getType().ordinal());
                var.writeToBuffer(buf);
            }
        }
    };

    protected T value;
    protected T oldValue;

    protected boolean shouldSync = false;
    protected String saveId;

    protected ScriptVariable(ScriptVariableManager manager, T originalValue) {
        this.value = this.oldValue = originalValue;
        manager.add(this);
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    protected final void recieve(Update<?> update) {
        value = (T)update.data;
        markSynced();
    }

    public boolean isChanged() {
        return !oldValue.equals(value);
    }

    public void markSynced() {
        oldValue = value;
    }

    public ScriptVariable<T> sync() {
        shouldSync = true;
        return this;
    }

    public ScriptVariable<T> save(String id) {
        saveId = id;
        return this;
    }

    public abstract Update<T> getUpdate(int id);

    protected static abstract class Update<T> {
        protected int id;
        protected T data;

        protected Update(int id, T data) {
            this.id = id;
            this.data = data;
        }

        protected Update(ByteBuf buf) {
            this.readFromBuffer(buf);
        }

        public abstract void writeToBuffer(ByteBuf buffer);

        public abstract void readFromBuffer(ByteBuf buffer);

        public abstract VariableType getType();
    }
}
