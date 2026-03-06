package diamondwalker.sscary.network;

import com.mojang.text2speech.Narrator;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record NarratorPacket(String text) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<NarratorPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "narrator"));

    public static final StreamCodec<ByteBuf, NarratorPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, NarratorPacket::text,
            NarratorPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final NarratorPacket packet, final IPayloadContext context) {
        Narrator narrator = Minecraft.getInstance().getNarrator().narrator;
        narrator.say(packet.text, true);
    }
}
