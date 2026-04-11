package diamondwalker.sscary.handler.internal;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.chunk.ChunkData;
import diamondwalker.sscary.registry.SScaryDataAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class BaseTrackingHandler {
    @SubscribeEvent
    private static void handlePlayerTick(PlayerTickEvent.Post event) {
        /*if (SScary.DEV_MODE) {
            Player player = event.getEntity();
            if (!player.level().isClientSide()) {
                ChunkPos pos = player.chunkPosition();
                ChunkAccess chunk = player.level().getChunk(pos.x, pos.z);
                ChunkData data = chunk.getData(SScaryDataAttachments.CHUNK.get());
                data.baseChunkTicks++;
                System.out.println("Ticks in chunk: " + data.baseChunkTicks);
                chunk.setUnsaved(true);
            }
        }*/
    }
}
