package diamondwalker.sscary.network;

import diamondwalker.sscary.TWAIS;
import diamondwalker.sscary.handler.feature.VisageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record VisageDisconnectPacket(String msg) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VisageDisconnectPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "visage_disconnect"));

    public static final StreamCodec<ByteBuf, VisageDisconnectPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, VisageDisconnectPacket::msg,
            VisageDisconnectPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final VisageDisconnectPacket packet, final IPayloadContext context) {
        context.connection().disconnect(Component.literal(packet.msg()));
        VisageHandler.doMenuScare();
    }
}
