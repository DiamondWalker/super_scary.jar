package diamondwalker.twais.handler.feature;

import diamondwalker.twais.network.ScreenColorShaderPacket;
import diamondwalker.twais.network.ScreenFlashPacket;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber
public class VisageHandler {
    @SubscribeEvent
    public static void handlePlayerHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            event.setCanceled(true);
            PacketDistributor.sendToPlayer(player, new ScreenFlashPacket(1.0f, 0.0f, 0.0f)); // TODO: add a cooldown to this so it doesn't spam you
        }
    }
}
