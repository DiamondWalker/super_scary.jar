package diamondwalker.twais.network;

import diamondwalker.twais.data.client.ClientData;
import diamondwalker.twais.data.client.ScreenFlashData;
import diamondwalker.twais.registry.TWAISSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DarkWorldPacket(boolean enable) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DarkWorldPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("twais", "dark_world"));

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
