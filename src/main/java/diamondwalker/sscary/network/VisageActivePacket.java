package diamondwalker.sscary.network;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record VisageActivePacket(boolean active) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VisageActivePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "visage_active"));

    public static final StreamCodec<ByteBuf, VisageActivePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, VisageActivePacket::active,
            VisageActivePacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final VisageActivePacket packet, final IPayloadContext context) {
        ClientData.get().setVisageActive(packet.active);
    }
}
