package diamondwalker.sscary.network;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PartyTimePacket(boolean active) implements CustomPacketPayload {
    public static final Type<PartyTimePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "party_time"));

    public static final StreamCodec<ByteBuf, PartyTimePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, PartyTimePacket::active,
            PartyTimePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final PartyTimePacket packet, final IPayloadContext context) {
        ClientData.get().wackyColors = packet.active();
    }
}
