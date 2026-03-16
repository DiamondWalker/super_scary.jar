package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.entity.entity.watchtower.EntityWatchtower;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CalculatePlayerTurnEvent;

import java.util.function.Supplier;

@EventBusSubscriber
public class MouseLockHandler {
    @SubscribeEvent
    private static void handleMouseLocking(CalculatePlayerTurnEvent event) {
        EntityWatchtower tower = ClientData.get().tower;
        if (tower != null) {
            if (!tower.isRemoved() && tower.canSee(Minecraft.getInstance().player)) {
                event.setMouseSensitivity(0);
            } else {
                ClientData.get().tower = null;
            }
        }
    }
}
