package diamondwalker.sscary.network;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.gui.screen.VisageDeathScreen;
import diamondwalker.sscary.handler.feature.VisageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class VisageDisconnectPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VisageDisconnectPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "visage_disconnect"));

    public static final VisageDisconnectPacket INSTANCE = new VisageDisconnectPacket();
    public static final StreamCodec<ByteBuf, VisageDisconnectPacket> CODEC = StreamCodec.unit(INSTANCE);

    private VisageDisconnectPacket() {}

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final VisageDisconnectPacket packet, final IPayloadContext context) {
        Minecraft.getInstance().disconnect();
        Minecraft.getInstance().setScreen(new VisageDeathScreen());
    }
}
