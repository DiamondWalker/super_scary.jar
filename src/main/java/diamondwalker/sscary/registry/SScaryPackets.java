package diamondwalker.sscary.registry;

import diamondwalker.sscary.Config;
import diamondwalker.sscary.SScary;
import diamondwalker.sscary.network.*;
import diamondwalker.sscary.network.debug.PathRenderingPacket;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.PathfindingDebugPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class SScaryPackets {
    @SubscribeEvent
    private static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(StaticScreenPacket.TYPE, StaticScreenPacket.CODEC, StaticScreenPacket::handle);
        registrar.playToClient(ScreenColorShaderPacket.TYPE, ScreenColorShaderPacket.CODEC, ScreenColorShaderPacket::handle);
        registrar.playToClient(VisageFlashPacket.TYPE, VisageFlashPacket.CODEC, VisageFlashPacket::handle);
        registrar.playToClient(VisageActivePacket.TYPE, VisageActivePacket.CODEC, VisageActivePacket::handle);
        registrar.playToClient(SilencePacket.TYPE, SilencePacket.CODEC, SilencePacket::handle);
        registrar.playToClient(VisageDisconnectPacket.TYPE, VisageDisconnectPacket.CODEC, VisageDisconnectPacket::handle);
        registrar.playToClient(AddSyncedScriptPacket.TYPE, AddSyncedScriptPacket.CODEC, AddSyncedScriptPacket::handle);
        registrar.playToClient(RemoveSyncedScriptPacket.TYPE, RemoveSyncedScriptPacket.CODEC, RemoveSyncedScriptPacket::handle);
        registrar.playToClient(UpdateSyncedScriptPacket.TYPE, UpdateSyncedScriptPacket.CODEC, UpdateSyncedScriptPacket::handle);
        registrar.playToClient(NarratorPacket.TYPE, NarratorPacket.CODEC, NarratorPacket::handle);

        if (Config.EXTRA_DEBUG_INFO.get()) registerDebugPackets(registrar);
    }

    private static void registerDebugPackets(final PayloadRegistrar registrar) {
        registrar.playToClient(PathRenderingPacket.TYPE, PathRenderingPacket.STREAM_CODEC, PathRenderingPacket::handle);
    }
}
