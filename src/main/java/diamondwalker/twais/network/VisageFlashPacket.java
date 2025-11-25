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

public record VisageFlashPacket(boolean isRed) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VisageFlashPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("twais", "visage_flash"));

    public static final StreamCodec<ByteBuf, VisageFlashPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, VisageFlashPacket::isRed,
            VisageFlashPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final VisageFlashPacket packet, final IPayloadContext context) {
        if (packet.isRed()) {
            ClientData.get().flash = new ScreenFlashData(1.0f, 0.0f, 0.0f, System.nanoTime());
        } else {
            ClientData.get().flash = new ScreenFlashData(0.0f, 0.0f, 0.0f, System.nanoTime());
        }

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forLocalAmbience(TWAISSounds.VISAGE_TELEPORT.value(), 1.0f, 1.0f));
    }
}
