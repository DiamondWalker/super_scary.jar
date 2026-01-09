package diamondwalker.twais.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldUtil {
    public static List<LevelChunk> getBuildableChunks(ServerLevel level, boolean spawnProtection) {
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

                if (spawnProtection && player.getRespawnPosition() != null && player.getRespawnDimension() == level.dimension()) {
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

    public static SignWriter placeSign(Level level, BlockPos pos, double angle) {
        angle = Math.toDegrees(angle);
        int rot = Integer.valueOf(RotationSegment.convertToSegment((float)angle));
        return placeSign(level, pos, rot);
    }

    public static SignWriter placeSign(Level level, BlockPos pos, int rotation) {
        level.setBlock(pos, Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION, rotation % 16), 2);

        if (level.getBlockEntity(pos) instanceof SignBlockEntity sign) {
            return new SignWriter(sign);
        }

        return null;
    }

    public static class SignWriter {
        private final SignBlockEntity sign;
        private SignText frontText;
        private SignText backText;

        private SignWriter(SignBlockEntity sign) {
            this.sign = sign;
            frontText = new SignText();
            backText = new SignText();
        }

        public SignWriter setFrontLine(int i, String txt) {
            frontText = frontText.setMessage(i, Component.literal(txt));
            return this;
        }

        public SignWriter setBackLine(int i, String txt) {
            backText = backText.setMessage(i, Component.literal(txt));
            return this;
        }

        public SignWriter setFrontLine(int i, Component txt) {
            frontText = frontText.setMessage(i, txt);
            return this;
        }

        public SignWriter setBackLine(int i, Component txt) {
            backText = backText.setMessage(i, txt);
            return this;
        }

        public void write() {
            sign.setText(frontText, true);
            sign.setText(backText, false);
        }
    }
}
