package diamondwalker.sscary.network;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.NewScriptsClientData;
import diamondwalker.sscary.script.CalculationScript;
import diamondwalker.sscary.util.EnumCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static diamondwalker.sscary.script.CalculationScript.CalculationState;

public record CalculationStatePacket(int syncId, CalculationState state) implements CustomPacketPayload {
    private static final EnumCodec<CalculationState> STATE_CODEC = new EnumCodec<>(CalculationState.class);

    public static final CustomPacketPayload.Type<CalculationStatePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "calculation_punishment"));

    public static final StreamCodec<ByteBuf, CalculationStatePacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CalculationStatePacket::syncId,
            STATE_CODEC, CalculationStatePacket::state,
            CalculationStatePacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final CalculationStatePacket packet, final IPayloadContext context) {
        NewScriptsClientData data = ClientData.get().scripts;
        ((CalculationScript)data.getScriptFromSyncId(packet.syncId)).setState(packet.state);
    }
}
