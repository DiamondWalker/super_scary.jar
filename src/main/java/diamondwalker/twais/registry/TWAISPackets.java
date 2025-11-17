package diamondwalker.twais.registry;

import diamondwalker.twais.network.ScreenColorShaderPacket;
import diamondwalker.twais.network.ScreenFlashPacket;
import diamondwalker.twais.network.StaticScreenPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class TWAISPackets {
    @SubscribeEvent
    private static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(StaticScreenPacket.TYPE, StaticScreenPacket.CODEC, StaticScreenPacket::handle);
        registrar.playToClient(ScreenColorShaderPacket.TYPE, ScreenColorShaderPacket.CODEC, ScreenColorShaderPacket::handle);
        registrar.playToClient(ScreenFlashPacket.TYPE, ScreenFlashPacket.CODEC, ScreenFlashPacket::handle);
    }
}
