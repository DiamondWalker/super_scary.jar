package diamondwalker.twais.registry;

import diamondwalker.twais.network.ScreenColorShaderPacket;
import diamondwalker.twais.network.StaticScreenPacket;
import diamondwalker.twais.network.VisageFlashPacket;
import diamondwalker.twais.network.VisageFogPacket;
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
        registrar.playToClient(VisageFlashPacket.TYPE, VisageFlashPacket.CODEC, VisageFlashPacket::handle);
        registrar.playToClient(VisageFogPacket.TYPE, VisageFogPacket.CODEC, VisageFogPacket::handle);
    }
}
