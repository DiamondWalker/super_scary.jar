package diamondwalker.sscary.network;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.registry.CustomRegistries;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.script.ScriptType;
import diamondwalker.sscary.script.variable.ScriptVariable;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.Optional;

public record AddSyncedScriptPacket(int scriptType, int scriptId, List<ScriptVariable.Update<?>> data) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AddSyncedScriptPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "add_synced_script"));

    public static final StreamCodec<ByteBuf, AddSyncedScriptPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AddSyncedScriptPacket::scriptType,
            ByteBufCodecs.INT, AddSyncedScriptPacket::scriptId,
            ScriptVariable.STREAM_CODEC, AddSyncedScriptPacket::data,
            AddSyncedScriptPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final AddSyncedScriptPacket packet, final IPayloadContext context) {
        ScriptType<?> type = CustomRegistries.SCRIPT_REGISTRY.byIdOrThrow(packet.scriptType);
        Script script = type.buildForClient(packet.scriptId);
        script.variableManager.receiveUpdates(packet.data);
        ClientData.get().scripts.addScript(script);
    }
}
