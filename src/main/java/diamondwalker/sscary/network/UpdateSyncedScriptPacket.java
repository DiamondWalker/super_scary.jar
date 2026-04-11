package diamondwalker.sscary.network;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.NewScriptsClientData;
import diamondwalker.sscary.script.variable.ScriptVariable;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record UpdateSyncedScriptPacket(int syncId, List<ScriptVariable.Update<?>> updates) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateSyncedScriptPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "update_synced_script"));

    public static final StreamCodec<ByteBuf, UpdateSyncedScriptPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateSyncedScriptPacket::syncId,
            ScriptVariable.STREAM_CODEC, UpdateSyncedScriptPacket::updates,
            UpdateSyncedScriptPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final UpdateSyncedScriptPacket packet, final IPayloadContext context) {
        NewScriptsClientData data = ClientData.get().scripts;
        data.getScriptFromSyncId(packet.syncId).variableManager.receiveUpdates(packet.updates);
    }
}
