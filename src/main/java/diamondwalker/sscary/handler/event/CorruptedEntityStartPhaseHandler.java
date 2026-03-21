package diamondwalker.sscary.handler.event;

import diamondwalker.sscary.data.PermanentSaveData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.script.CorruptedIntroScript;
import diamondwalker.sscary.util.ChatUtil;
import diamondwalker.sscary.util.ScriptBuilder;
import diamondwalker.sscary.util.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

@EventBusSubscriber
public class CorruptedEntityStartPhaseHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);
        long time = data.progression.getTimeInWorld();
        PermanentSaveData persistentData = PermanentSaveData.getOrCreateInstance();

        if (persistentData.getCorruptedAngered()) {
            if (!data.progression.hasBeenAngered() && !data.scripts.hasLock("corrupted_entity")) {
                ScriptBuilder builder = new ScriptBuilder(server, "corrupted_entity")
                        .rest(1800)
                        .chatMessageForAll(ChatUtil.getJoinMessage(ChatUtil.CORRUPTED_ENTITY_NAME));
                if (persistentData.getCorruptedRemembered()) {
                    builder
                            .rest(90)
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "still here bro").withStyle(ChatFormatting.DARK_RED))
                            .action((serv) -> WorldData.get(serv).progression.startAnger(serv))
                            .startScript();
                } else {
                    builder
                            .rest(150)
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "dude"))
                            .rest(90)
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "did you really think id forget what you did just cuz you ran off to another world?"))
                            .rest(125)
                            .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "what a pussy lmfao").withStyle(ChatFormatting.DARK_RED))
                            .action((serv) -> {
                                WorldData.get(serv).progression.startAnger(serv);
                                PermanentSaveData permData = PermanentSaveData.getOrCreateInstance();
                                permData.setCorruptedRemembered(true);
                                permData.saveChanges();
                            })
                            .startScript();
                }
            }
            return;
        }

        if (!data.progression.hasBeenAngered() && (time - 18_000L) % 24_000L == 0 && !data.scripts.hasLock("corrupted_entity")) {
            ServerLevel level = server.overworld();
            List<LevelChunk> possibleChunks = WorldUtil.getBuildableChunks(level, true);
            if (possibleChunks.isEmpty()) return;

            if (!possibleChunks.isEmpty()) {
                LevelChunk chosenChunk = possibleChunks.get(level.getRandom().nextInt(possibleChunks.size()));
                ChunkPos chosenPos = chosenChunk.getPos();
                int x = chosenPos.getMinBlockX() + level.random.nextInt(16);
                int z = chosenPos.getMinBlockZ() + level.random.nextInt(16);
                int y = Integer.MIN_VALUE;

                int minX = x - 12;
                int maxX = x + 12;
                int minZ = z - 12;
                int maxZ = z + 12;

                for (int cx = minX; cx <= maxX; cx++) {
                    for (int cz = minZ; cz <= maxZ; cz++) {
                        y = Math.max(y, level.getHeight(Heightmap.Types.MOTION_BLOCKING, cx, cz));
                    }
                }

                if (y < level.getMaxBuildHeight() - 15) {
                    for (int cx = minX; cx <= maxX; cx++) {
                        for (int cz = minZ; cz <= maxZ; cz++) {
                            for (int cy = 0; cy <= 12; cy++) {
                                level.setBlock(new BlockPos(cx, y + cy, cz), Blocks.COBBLESTONE.defaultBlockState(), 2);
                            }
                        }
                    }

                    WorldData.get(server).newScripts.startScript(new CorruptedIntroScript(server, new BlockPos(x, y, z)));
                    for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                        player.connection.send(new ClientboundSoundPacket(SoundEvents.AMBIENT_CAVE, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 64.0F, 1.0F, server.overworld().getRandom().nextLong()));
                    }
                }
            }
        }
    }
}
