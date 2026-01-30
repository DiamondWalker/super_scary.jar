package diamondwalker.sscary.network;

import diamondwalker.sscary.TWAIS;
import diamondwalker.sscary.data.client.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SilencePacket(boolean active) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SilencePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "silence"));

    public static final StreamCodec<ByteBuf, SilencePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SilencePacket::active,
            SilencePacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SilencePacket packet, final IPayloadContext context) {
        ClientData.get().setSilenced(packet.active);
    }
}
