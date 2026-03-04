package diamondwalker.sscary.network;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.registry.CustomRegistries;
import diamondwalker.sscary.script.ScriptType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RemoveSyncedScriptPacket(int scriptId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RemoveSyncedScriptPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "remove_synced_script"));

    public static final StreamCodec<ByteBuf, RemoveSyncedScriptPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, RemoveSyncedScriptPacket::scriptId,
            RemoveSyncedScriptPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final RemoveSyncedScriptPacket packet, final IPayloadContext context) {
        ClientData.get().scripts.removeScript(ClientData.get().scripts.getScriptFromSyncId(packet.scriptId));
    }
}
