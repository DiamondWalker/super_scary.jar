package diamondwalker.sscary.registry;

import diamondwalker.sscary.network.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
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
        registrar.playToClient(DarkWorldPacket.TYPE, DarkWorldPacket.CODEC, DarkWorldPacket::handle);
        registrar.playToClient(PartyTimePacket.TYPE, PartyTimePacket.CODEC, PartyTimePacket::handle);
        registrar.playToClient(SilencePacket.TYPE, SilencePacket.CODEC, SilencePacket::handle);
        registrar.playToClient(VisageDisconnectPacket.TYPE, VisageDisconnectPacket.CODEC, VisageDisconnectPacket::handle);
        registrar.playToClient(CalculationStatePacket.TYPE, CalculationStatePacket.CODEC, CalculationStatePacket::handle);
        registrar.playToClient(AddSyncedScriptPacket.TYPE, AddSyncedScriptPacket.CODEC, AddSyncedScriptPacket::handle);
        registrar.playToClient(RemoveSyncedScriptPacket.TYPE, RemoveSyncedScriptPacket.CODEC, RemoveSyncedScriptPacket::handle);
        registrar.playToClient(UpdateSyncedScriptPacket.TYPE, UpdateSyncedScriptPacket.CODEC, UpdateSyncedScriptPacket::handle);
    }
}
