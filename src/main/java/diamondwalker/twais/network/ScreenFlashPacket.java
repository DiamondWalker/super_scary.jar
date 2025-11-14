package diamondwalker.twais.network;

import diamondwalker.twais.data.client.ClientData;
import diamondwalker.twais.data.client.ScreenFlashData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ScreenFlashPacket(float red, float green, float blue) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ScreenFlashPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("twais", "screen_flash"));

    public static final StreamCodec<ByteBuf, ScreenFlashPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ScreenFlashPacket::red,
            ByteBufCodecs.FLOAT, ScreenFlashPacket::green,
            ByteBufCodecs.FLOAT, ScreenFlashPacket::blue,
            ScreenFlashPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ScreenFlashPacket packet, final IPayloadContext context) {
        ClientData.get().flash = new ScreenFlashData(packet.red(), packet.green(), packet.blue(), System.nanoTime());
    }
}
