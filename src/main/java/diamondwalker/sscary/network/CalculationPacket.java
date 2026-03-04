package diamondwalker.sscary.network;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CalculationPacket(boolean active) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CalculationPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "calculation"));

    public static final StreamCodec<ByteBuf, CalculationPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, CalculationPacket::active,
            CalculationPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final CalculationPacket packet, final IPayloadContext context) {
        ClientData.get().calculationAngry = packet.active();
    }
}
