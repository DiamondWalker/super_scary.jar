package diamondwalker.sscary.network;

import diamondwalker.sscary.TWAIS;
import diamondwalker.sscary.data.client.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DarkWorldPacket(boolean enable) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DarkWorldPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "dark_world"));

    public static final StreamCodec<ByteBuf, DarkWorldPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, DarkWorldPacket::enable,
            DarkWorldPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final DarkWorldPacket packet, final IPayloadContext context) {
        ClientData.get().darkWorld = packet.enable();
    }
}
