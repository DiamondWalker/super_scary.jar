package diamondwalker.sscary.handler.feature;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.entity.entity.construct.EntityConstruct;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.List;

/*@EventBusSubscriber
public class ConstructHandler {
    @SubscribeEvent
    private static void handleTick(ClientTickEvent.Post event) {
        List<EntityConstruct> constructs = ClientData.get().constructs;

        boolean screenShake = false;
        int i = 0;
        while (i < constructs.size()) {
            EntityConstruct construct = constructs.get(i);
            if (construct.isRemoved()) {
                constructs.remove(i);
            } else {
                if (construct.getTarget() != null && construct.getTarget() == Minecraft.getInstance().player) {
                    screenShake = true;
                }
                i++;
            }
        }

        if (screenShake) {
            Player player = Minecraft.getInstance().player;

            player.setXRot(player.getXRot() + player.getRandom().nextFloat() - 0.5f);
            player.setYRot(player.getYRot() + player.getRandom().nextFloat() - 0.5f);
        }
    }
}*/
