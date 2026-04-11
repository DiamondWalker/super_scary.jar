package diamondwalker.sscary.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class EnumCodec <T extends Enum<T>> implements StreamCodec<ByteBuf, T> {
    private final T[] values;

    public EnumCodec(Class<T> theEnum) {
        values = theEnum.getEnumConstants();
    }

    public T decode(ByteBuf p_320319_) {
        return values[p_320319_.readUnsignedByte()];
    }

    public void encode(ByteBuf p_320669_, T p_320205_) {
        p_320669_.writeByte(p_320205_.ordinal());
    }
}
