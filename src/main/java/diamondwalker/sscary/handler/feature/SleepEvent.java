package diamondwalker.sscary.handler.feature;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;

@EventBusSubscriber
public class SleepEvent {
    @SubscribeEvent
    public static void tryToSleep(CanPlayerSleepEvent event) {
        Player player = event.getEntity();

        if (player.getRandom().nextInt(10) == 0) {
            event.setProblem(Player.BedSleepingProblem.OTHER_PROBLEM);
            player.displayClientMessage(Component.translatable("block.twais.bed.scare"), true);
        }
    }
}
