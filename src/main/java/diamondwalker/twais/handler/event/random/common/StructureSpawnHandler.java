package diamondwalker.twais.handler.event.random.common;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

@EventBusSubscriber
public class StructureSpawnHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (data.progression.hasBeenAngered()) { // since this is a more passive event I don't think it needs the cooldown check
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(WorldData.COMMON_CHANCE) == 0) {
                ServerLevel level = server.overworld();
                List<LevelChunk> chunks = WorldUtil.getBuildableChunks(level, false);
                if (chunks.isEmpty()) return;

                int selection = random.nextInt(3); // 0 = """tower""", 1 = hole, 2 = under construction
                for (int i = 0; i < 10; i++) {
                    LevelChunk selectedChunk = chunks.get(random.nextInt(chunks.size()));
                    switch (selection) {
                        case 0: {
                            if (buildMonolith(level, selectedChunk, random)) return;
                            break;
                        }
                        case 1: {
                            if (buildHole(level, selectedChunk, random)) return;
                            break;
                        }
                        case 2: {
                            if (buildScaffold(level, selectedChunk, random)) return;
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean buildMonolith(ServerLevel level, LevelChunk chunk, RandomSource rand) {
        ArrayList<Vec3i> blocks = new ArrayList<>();

        // spheres
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 4; z++) {
                blocks.add(new Vec3i(x + 1, 0, z + 1));
                blocks.add(new Vec3i(x + 8, 0, z + 1));
                blocks.add(new Vec3i(x + 1, 4, z + 1));
                blocks.add(new Vec3i(x + 8, 4, z + 1));
            }
        }
        for (int y = 1; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                for (int z = 0; z < 6; z++) {
                    if (!((x == 0 || x == 4) && (z == 0 || z == 5))) {
                        blocks.add(new Vec3i(x, y, z));
                        blocks.add(new Vec3i(x + 7, y, z));
                    }
                }
            }
        }

        // cylinder
        for (int y = 0; y < 16; y++) {
            blocks.add(new Vec3i(5, 3 + y, 2));
            blocks.add(new Vec3i(5, 3 + y, 3));
            blocks.add(new Vec3i(6, 3 + y, 2));
            blocks.add(new Vec3i(6, 3 + y, 3));

            if (y > 0 && y < 15) {
                for (int x = 0; x < 4; x++) {
                    for (int z = 0; z < 4; z++) {
                        boolean flag = y > 1 && y < 13;
                        if (flag || !((x == 0 || x == 3) && (z == 0 || z == 3))) {
                            blocks.add(new Vec3i(4 + x, 3 + y, 1 + z));
                        }
                        if (flag) {
                            blocks.add(new Vec3i(5, 3 + y, 0));
                            blocks.add(new Vec3i(3, 3 + y, 2));

                            blocks.add(new Vec3i(5, 3 + y, 5));
                            blocks.add(new Vec3i(3, 3 + y, 3));

                            blocks.add(new Vec3i(6, 3 + y, 0));
                            blocks.add(new Vec3i(8, 3 + y, 2));

                            blocks.add(new Vec3i(6, 3 + y, 5));
                            blocks.add(new Vec3i(8, 3 + y, 3));
                        }
                    }
                }
            }
        }

        boolean rotate = rand.nextBoolean();
        int startX = chunk.getPos().getMinBlockX() + rand.nextInt(rotate ? 10 : 4);
        int startZ = chunk.getPos().getMinBlockZ() + rand.nextInt(rotate ? 4 : 10);
        int startY = level.getMinBuildHeight();
        for (int i = 0; i < blocks.size(); i++) {
            Vec3i curr = blocks.get(i);
            if (rotate) {
                curr = new Vec3i(curr.getZ(), curr.getY(), curr.getX());
                blocks.set(i, curr);
            }

            if (curr.getY() == 0) {
                startY = Math.max(startY, level.getHeight(Heightmap.Types.MOTION_BLOCKING, startX + curr.getX(), startZ + curr.getZ()));
            }
        }

        for (Vec3i pos : blocks) {
            level.setBlock(new BlockPos(startX + pos.getX(), startY+ pos.getY(), startZ + pos.getZ()), Blocks.COBBLESTONE.defaultBlockState(), 2);
        }

        return true;
    }

    private static boolean buildHole(ServerLevel level, LevelChunk chunk, RandomSource rand) {
        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z); y >= chunk.getMinBuildHeight(); y--) {
                    level.setBlock(new BlockPos(startX + x, y, startZ + z), Blocks.AIR.defaultBlockState(), 2);
                }
            }
        }

        HashMap<Direction, BlockPos> idealDirections = new HashMap<>();
        for (Direction dir : Direction.values()) {
            if (dir.getNormal().getY() == 0) { // only horizontal values
                BlockPos pos = new BlockPos(startX + 8, 0, startZ + 8).offset(dir.getNormal().multiply(20));
                pos = new BlockPos(Mth.clamp(pos.getX(), startX - 1, startX + 16), 0, Mth.clamp(pos.getZ(), startZ - 1, startZ + 16));
                pos = pos.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()));
                if (!(level.getBlockState(pos).getBlock() instanceof LeavesBlock)) {
                    idealDirections.put(dir, pos);
                }
            }
        }

        Direction dir;
        BlockPos pos;
        if (idealDirections.isEmpty()) {
            dir = Direction.from2DDataValue(rand.nextInt(4));
            pos = new BlockPos(startX + 8, 0, startZ + 8).offset(dir.getNormal().multiply(20));
            pos = new BlockPos(Mth.clamp(pos.getX(), startX - 1, startX + 16), 0, Mth.clamp(pos.getZ(), startZ - 1, startZ + 16));
            pos = pos.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ()));
        } else {
            Map.Entry<Direction, BlockPos>[] entries = idealDirections.entrySet().toArray(new Map.Entry[0]);
            Map.Entry<Direction, BlockPos> entry = entries[rand.nextInt(entries.length)];
            dir = entry.getKey();
            pos = entry.getValue();
        }

        int dirval = switch (dir) {
            case DOWN, UP -> throw new IllegalStateException();
            case SOUTH -> 0;
            case NORTH -> 8;
            case WEST -> 4;
            case EAST -> 12;
        };

        WorldUtil.placeSign(level, pos, dirval)
                .setFrontLine(1, "Hole")
                .write();

        return true;
    }

    private static boolean buildScaffold(ServerLevel level, LevelChunk chunk, RandomSource rand) {
        int startX = chunk.getPos().getMinBlockX() + rand.nextInt(6);
        int startZ = chunk.getPos().getMinBlockZ() + rand.nextInt(6);

        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;

        for (int y = level.getMaxBuildHeight(); y >= level.getMinBuildHeight(); y--) {
            if (flag1) {
                BlockPos pos = new BlockPos(startX, y, startZ);
                BlockState state = level.getBlockState(pos);
                if (state.blocksMotion() && !(state.getBlock() instanceof LeavesBlock)) {
                    flag1 = false;
                } else {
                    level.setBlock(pos, Blocks.OAK_LOG.defaultBlockState(), 2);
                }
            }

            if (flag2) {
                BlockPos pos = new BlockPos(startX + 10, y, startZ);
                BlockState state = level.getBlockState(pos);
                if (state.blocksMotion() && !(state.getBlock() instanceof LeavesBlock)) {
                    flag2 = false;
                } else {
                    level.setBlock(pos, Blocks.OAK_LOG.defaultBlockState(), 2);
                }
            }

            if (flag3) {
                BlockPos pos = new BlockPos(startX, y, startZ + 10);
                BlockState state = level.getBlockState(pos);
                if (state.blocksMotion() && !(state.getBlock() instanceof LeavesBlock)) {
                    flag3 = false;
                } else {
                    level.setBlock(pos, Blocks.OAK_LOG.defaultBlockState(), 2);
                }
            }

            if (flag4) {
                BlockPos pos = new BlockPos(startX + 10, y, startZ + 10);
                BlockState state = level.getBlockState(pos);
                if (state.blocksMotion() && !(state.getBlock() instanceof LeavesBlock)) {
                    flag4 = false;
                } else {
                    level.setBlock(pos, Blocks.OAK_LOG.defaultBlockState(), 2);
                }
            }

            if (flag1 && flag2 && flag3 && flag4 && (level.getMaxBuildHeight() - y - 1) % 10 == 0) {
                for (int x = 0; x < 10; x++) {
                    level.setBlock(new BlockPos(startX + x, y, startZ), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X), 2);
                    level.setBlock(new BlockPos(startX + x, y, startZ + 10), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X), 2);
                }
                for (int z = 0; z < 10; z++) {
                    level.setBlock(new BlockPos(startX, y, startZ + z), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z), 2);
                    level.setBlock(new BlockPos(startX + 10, y, startZ + z), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z), 2);
                }
            }

            if (!flag1 && !flag2 && !flag3 && !flag4) {
                break;
            }
        }

        HashMap<Direction, BlockPos> idealDirections = new HashMap<>();
        for (Direction dir : Direction.values()) {
            if (dir.getNormal().getY() == 0) { // only horizontal values
                BlockPos pos = new BlockPos(startX + 5, 0, startZ + 5).offset(dir.getNormal().multiply(9));
                pos = pos.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()));
                if (!(level.getBlockState(pos).getBlock() instanceof LeavesBlock)) {
                    idealDirections.put(dir, pos);
                }
            }
        }

        Direction dir;
        BlockPos pos;
        if (idealDirections.isEmpty()) {
            dir = Direction.from2DDataValue(rand.nextInt(4));
            pos = new BlockPos(startX + 5, 0, startZ + 5).offset(dir.getNormal().multiply(9));
            pos = pos.atY(level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ()));
        } else {
            Map.Entry<Direction, BlockPos>[] entries = idealDirections.entrySet().toArray(new Map.Entry[0]);
            Map.Entry<Direction, BlockPos> entry = entries[rand.nextInt(entries.length)];
            dir = entry.getKey();
            pos = entry.getValue();
        }

        int dirval = switch (dir) {
            case DOWN, UP -> throw new IllegalStateException();
            case SOUTH -> 0;
            case NORTH -> 8;
            case WEST -> 4;
            case EAST -> 12;
        };

        WorldUtil.placeSign(level, pos, dirval)
                .setFrontLine(1, "Under")
                .setFrontLine(2, "Construction")
                .write();

        return true;
    }

    private static boolean buildHeadOnPike(ServerLevel level, LevelChunk chunk) {
        level.setBlock(
                new BlockPos(chunk.getPos().getMinBlockX(), 200, chunk.getPos().getMinBlockZ()),
                Blocks.PLAYER_HEAD.defaultBlockState(),
                2
        );

        return true;
    }
}
