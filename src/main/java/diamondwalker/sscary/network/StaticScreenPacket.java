package diamondwalker.sscary.network;

import diamondwalker.sscary.TWAIS;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.StaticData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StaticScreenPacket(int time) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StaticScreenPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TWAIS.MODID, "static_screen"));

    public static final StreamCodec<ByteBuf, StaticScreenPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, StaticScreenPacket::time,
            StaticScreenPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final StaticScreenPacket packet, final IPayloadContext context) {
        ClientData.get().staticData = new StaticData(1.0f, 1.0f, packet.time(), true);
    }
}
