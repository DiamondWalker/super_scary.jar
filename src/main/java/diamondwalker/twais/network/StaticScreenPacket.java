package diamondwalker.twais.network;

import diamondwalker.twais.data.client.ClientData;
import diamondwalker.twais.data.client.ScreenFlashData;
import diamondwalker.twais.util.shader.EnumShaderLayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StaticScreenPacket(int time) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StaticScreenPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("twais", "static_screen"));

    public static final StreamCodec<ByteBuf, StaticScreenPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, StaticScreenPacket::time,
            StaticScreenPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final StaticScreenPacket packet, final IPayloadContext context) {
        // TODO: handle
    }
}
