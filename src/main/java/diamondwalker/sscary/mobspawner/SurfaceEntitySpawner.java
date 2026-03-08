package diamondwalker.sscary.mobspawner;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.EventHooks;

import java.util.List;

public class SurfaceEntitySpawner implements CustomSpawner {
    private final int probability;
    private final EntityType<? extends Monster> type;
    private final boolean spawnsAtDay;
    private final boolean spawnsAtNight;
    private final double minDist;
    private final double maxDist;

    public SurfaceEntitySpawner(EntityType<? extends Monster> type, int chance, boolean spawnsAtDay, boolean spawnsAtNight, double minDistance, double maxDistance) {
        this.type = type;
        this.probability = chance;
        this.spawnsAtDay = spawnsAtDay;
        this.spawnsAtNight = spawnsAtNight;
        this.minDist = minDistance;
        this.maxDist = maxDistance;

        if (minDist > maxDist) throw new IllegalStateException("Minimum spawn distance " + minDist + " should not be greater than maximum distance " + maxDist);
    }

    @Override
    public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (level.isDay() ? spawnsAtDay : spawnsAtNight) {
            if (type.getCategory().isFriendly() ? spawnFriendlies : spawnEnemies) {
                if (level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    if (level.random.nextInt(probability) == 0) {
                        if (tryToSpawn(type, level, level.random)) return 1;
                    }
                }
            }
        }

        return 0;
    }

    private boolean tryToSpawn(EntityType<? extends Monster> type, ServerLevel level, RandomSource random) {
        List<ServerPlayer> players = level.players().stream().filter(p -> !p.isSpectator()).toList();
        if (players.isEmpty()) return false;

        for (int i = 0; i < 30; i++) {
            ServerPlayer player = players.get(random.nextInt(players.size()));
            double magnitude = random.nextDouble() * (maxDist - minDist) + minDist;
            double angle = random.nextDouble() * Math.PI * 2;

            BlockPos.MutableBlockPos pos = BlockPos.containing(player.getX() + magnitude * Math.cos(angle), 0, player.getZ() + magnitude * Math.sin(angle)).mutable();

            if (level.hasChunkAt(pos.getX(), pos.getZ())) {
                pos.setY(level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()));

                BlockState block = level.getBlockState(pos);
                if (NaturalSpawner.isValidEmptySpawnBlock(level, pos, block, block.getFluidState(), type)) {
                    if (level.getBrightness(LightLayer.BLOCK, pos) <= 0 && Monster.checkAnyLightMonsterSpawnRules(type, level, MobSpawnType.NATURAL, pos, random)) {
                        Monster mob = type.create(level);
                        if (mob != null) {
                            mob.moveTo(pos.getBottomCenter(), level.random.nextFloat() * 360.0F, 0.0F);
                            if (EventHooks.checkSpawnPosition(mob, level, MobSpawnType.NATURAL)) {
                                mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null);
                                level.addFreshEntity(mob);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }


    /*@Override
    public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        int spawned = 0;
        RandomSource random = level.random;

        if (spawnEnemies) {
            if (random.nextInt(300) == 0) {
                if (tryToSpawn(SScaryEntities.WATCHTOWER.get(), level)) spawned++;
            }

            if (random.nextInt(600) == 0) {
                if (tryToSpawn(SScaryEntities.CONSTRUCT.get(), level)) spawned++;
            }
        }

        return spawned;
    }

    private static boolean tryToSpawn(EntityType<?> entity, ServerLevel level) {
        boolean flag1 = level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);

        List<LevelChunk> chunks = new ArrayList<>();
        for (ChunkHolder holder : level.getChunkSource().chunkMap.getChunks()) {
            LevelChunk chunk = holder.getTickingChunk();
            if (chunk != null) {
                ChunkPos chunkpos = chunk.getPos();
                if ((level.isNaturalSpawningAllowed(chunkpos) && !level.getChunkSource().chunkMap.getPlayersCloseForSpawning(chunkpos).isEmpty()) || level.getChunkSource().chunkMap.getDistanceManager().shouldForceTicks(chunkpos.toLong())) {
                    if (flag1 && level.getWorldBorder().isWithinBounds(chunkpos)) {
                        chunks.add(chunk);
                    }
                }
            }
        }

        if (chunks.isEmpty()) return false;
        for (int i = 0; i < 50; i++) {
            if (tryToSpawnIn(entity, level, chunks.get(level.random.nextInt(chunks.size())))) {
                 return true;
            }
        }
        return false;
    }

    private static boolean tryToSpawnIn(EntityType<?> entity, ServerLevel level, LevelChunk chunk) {
        ChunkPos chunkpos = chunk.getPos();
        int x = chunkpos.getMinBlockX() + level.random.nextInt(16);
        int z = chunkpos.getMinBlockZ() + level.random.nextInt(16);
        int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) + 1;
        BlockPos pos = new BlockPos(x, y, z);



        int i = pos.getY();
        BlockState blockstate = chunk.getBlockState(pos);
        if (!blockstate.isRedstoneConductor(chunk, pos)) {
            double d0 = (double) pos.getX() + 0.5;
            double d1 = (double) pos.getZ() + 0.5;
            Player player = level.getNearestPlayer(d0, i, d1, -1.0, false);
            if (player != null) {
                if (
                        entity.canSummon() &&
                        SpawnPlacements.isSpawnPositionOk(entity, level, pos) &&
                        SpawnPlacements.checkSpawnRules(entity, level, MobSpawnType.NATURAL, pos, level.random) &&
                        level.noCollision(entity.getSpawnAABB((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5))
                ) {
                    if (entity.create(level) instanceof Mob mob) {
                        mob.moveTo(d0, i, d1, level.random.nextFloat() * 360.0F, 0.0F);
                        if (net.neoforged.neoforge.event.EventHooks.checkSpawnPosition(mob, level, MobSpawnType.NATURAL)) {
                            mob.finalizeSpawn(
                                    level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.NATURAL, null
                            );
                            level.addFreshEntityWithPassengers(mob);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }*/
}
