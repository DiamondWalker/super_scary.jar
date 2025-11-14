package diamondwalker.twais.network;

import diamondwalker.twais.handler.internal.ShaderHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ScreenColorShaderPacket(float red, float green, float blue) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ScreenColorShaderPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("twais", "screen_color_shader"));

    public static final StreamCodec<ByteBuf, ScreenColorShaderPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ScreenColorShaderPacket::red,
            ByteBufCodecs.FLOAT, ScreenColorShaderPacket::green,
            ByteBufCodecs.FLOAT, ScreenColorShaderPacket::blue,
            ScreenColorShaderPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ScreenColorShaderPacket packet, final IPayloadContext context) {
        ShaderHandler.setColorShader(packet.red(), packet.green(), packet.blue());
    }
}
