package diamondwalker.twais.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldUtil {
    public static List<LevelChunk> getBuildableChunks(ServerLevel level) {
        ArrayList<LevelChunk> possibleChunks = new ArrayList<>();

        CHUNK_LOOP: for (ChunkHolder holder : level.getChunkSource().chunkMap.getChunks()) {
            LevelChunk chunk = holder.getTickingChunk();
            if (chunk == null) continue;
            ChunkPos chunkPos = chunk.getPos();

            boolean closeEnough = false;

            for (ServerPlayer player : level.players()) {
                ChunkPos playerChunk = player.chunkPosition();
                int offsetX = chunkPos.x - playerChunk.x;
                int offsetZ = chunkPos.z - playerChunk.z;

                if (Math.abs(offsetX) <= 5 && Math.abs(offsetZ) <= 5) {
                    continue CHUNK_LOOP;
                }

                Vec3 look = player.getLookAngle();
                if (new Vec2(offsetX, offsetZ).normalized().dot(new Vec2((float)look.x, (float)look.z).normalized()) > 0) {
                    continue CHUNK_LOOP;
                }

                if (player.getRespawnPosition() != null && player.getRespawnDimension() == level.dimension()) {
                    ChunkPos spawnChunk = level.getChunk(player.getRespawnPosition()).getPos();

                    int spawnOffsetX = chunkPos.x - spawnChunk.x;
                    int spawnOffsetZ = chunkPos.z - spawnChunk.z;

                    if (Math.abs(spawnOffsetX) <= 5 && Math.abs(spawnOffsetZ) <= 5) {
                        continue CHUNK_LOOP;
                    }
                }

                if (Math.abs(offsetX) < 20 && Math.abs(offsetZ) < 20) {
                    closeEnough = true;
                }
            }

            if (closeEnough) possibleChunks.add(chunk);
        }

        return possibleChunks;
    }

    public static void placeSign(Level level, BlockPos pos, int rotation, Component[] lines) {
        placeSign(level, pos, rotation, lines, new Component[] {Component.empty(), Component.empty(), Component.empty(), Component.empty()});
    }

    public static void placeSign(Level level, BlockPos pos, int rotation, Component[] frontLines, Component[] backLines) {
        if (frontLines.length != 4) throw new IllegalArgumentException("Sign must be given 4 lines, not " + frontLines.length + "!");
        if (backLines.length != 4) throw new IllegalArgumentException("Sign must be given 4 lines, not " + backLines.length + "!");

        level.setBlock(pos, Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION, rotation % 16), 2);
        if (level.getBlockEntity(pos) instanceof SignBlockEntity sign) {
            SignText frontText = new SignText();
            SignText backText = new SignText();
            for (int i = 0; i < 4; i++) {
                frontText = frontText.setMessage(i, frontLines[i]);
                backText = backText.setMessage(i, backLines[i]);
            }
            sign.setText(frontText, true);
            sign.setText(backText, false);
        }
    }
}
