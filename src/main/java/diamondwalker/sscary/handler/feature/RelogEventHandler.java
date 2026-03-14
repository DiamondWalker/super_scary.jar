package diamondwalker.sscary.handler.feature;

import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.entity.entity.bizarrodude.EntityBizarroDude;
import diamondwalker.sscary.registry.SScaryEntities;
import diamondwalker.sscary.util.ChatUtil;
import diamondwalker.sscary.util.ScriptBuilder;
import diamondwalker.sscary.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class RelogEventHandler {
    @SubscribeEvent
    private static void handlePlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            MinecraftServer server = player.getServer();
            WorldData data = WorldData.get(server);
            if (data.progression.hasBeenAngered()) {
                if (data.bizarro.cooldown <= 0) {
                    data.bizarro.cooldown = 20 * 60 * 10; // 10 minutes

                    RandomSource rand = player.getRandom();

                    String name = player.getName().getString();
                    name = new StringBuilder(name).reverse().toString();

                    boolean bizarroOccurance = false;
                    if (data.bizarro.bizarroEncounters >= 3 && rand.nextInt(8) == 0) {
                        Vec3 dir = player.getLookAngle();
                        dir = new Vec3(dir.x, 0, dir.z).normalize();
                        if (dir.lengthSqr() > 0) { // in case you're looking straight up. idk if it's possible but just to be safe
                            Vec3 spawn = player.position().add(dir.scale(2.3));
                            BlockPos spawnPos = BlockPos.containing(spawn);
                            Level level = player.level();
                            if (level.getBlockState(spawnPos.below()).isSolid()) {
                                EntityBizarroDude entity = SScaryEntities.BIZZARO_DUDE.get().create(level);//.spawn((ServerLevel)player.level(), spawnPos, MobSpawnType.MOB_SUMMONED);
                                entity.moveTo(spawn);

                                if (level.noCollision(entity) && player.hasLineOfSight(entity)) {
                                    Vec3 relative = player.getEyePosition().subtract(entity.getEyePosition());
                                    double horizontalComponent = Math.sqrt(relative.x * relative.x + relative.z * relative.z);
                                    double yRot = Math.toDegrees(Math.atan2(relative.z, relative.x)) - 90;
                                    double xRot = -Math.toDegrees(Math.atan2(relative.y, horizontalComponent));
                                    entity.yHeadRot = entity.yBodyRot = (float)yRot;
                                    entity.setXRot((float)xRot);
                                    level.addFreshEntity(entity);
                                    bizarroOccurance = true;
                                }
                            }
                        }
                    } else {
                        if (rand.nextInt(5) == 0) {
                            new ScriptBuilder(server)
                                    .rest(20 + rand.nextInt(40 + 1))
                                    .chatMessageForAll(ChatUtil.getLeaveMessage(name))
                                    .startScript();
                            bizarroOccurance = true;
                        }

                        if (rand.nextInt(3) == 0) {
                            Level level = player.level();
                            BlockPos placedAt = null;
                            if (player.pick(7, 1.0f, false) instanceof BlockHitResult result && result.getType() == HitResult.Type.BLOCK) {
                                Direction dir = result.getDirection();
                                BlockPos hitBlock = result.getBlockPos();
                                BlockPos placeBlock;
                                if (level.getBlockState(hitBlock).canBeReplaced()) {
                                    placeBlock = hitBlock;
                                } else if (level.getBlockState(hitBlock).isSolid()) {
                                    placeBlock = hitBlock.offset(dir.getNormal());
                                } else {
                                    placeBlock = null;
                                }
                                if (placeBlock != null && level.getBlockState(placeBlock).canBeReplaced()) {
                                    if (dir == Direction.DOWN || !level.getBlockState(placeBlock.relative(dir.getOpposite())).isSolid()) { // this sign will be on the air
                                        for (Direction direction : Direction.values()) {
                                            if (direction != Direction.UP && direction != Direction.DOWN) {
                                                BlockPos pos = placeBlock.relative(direction);
                                                if (level.getBlockState(pos).isSolid()) {
                                                    dir = direction.getOpposite();
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    if (dir != Direction.DOWN && dir != Direction.UP) {
                                        BlockState state = Blocks.OAK_WALL_SIGN.defaultBlockState().setValue(WallSignBlock.FACING, dir);
                                        level.setBlock(placeBlock, state, 2);
                                        placedAt = placeBlock;
                                    } else if (dir == Direction.UP) {
                                        double angle = Math.toDegrees(Math.atan2(placeBlock.getCenter().z - player.getZ(), placeBlock.getCenter().x - player.getX()) + Math.PI / 2);
                                        int rot = Integer.valueOf(RotationSegment.convertToSegment((float)angle));
                                        BlockState state = Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION, rot);
                                        level.setBlock(placeBlock, state, 2);
                                        placedAt = placeBlock;
                                    }
                                }
                            }

                            if (placedAt == null) {
                                Vec3 dir = player.getLookAngle();
                                dir = new Vec3(dir.x, 0, dir.z).normalize();
                                if (dir.lengthSqr() > 0) { // in case you're looking straight up. idk if it's possible but just to be safe
                                    Vec3 placePos = player.position().add(dir.scale(3));
                                    BlockPos placeBlock = BlockPos.containing(placePos);
                                    BlockPos[] positionsToTry = new BlockPos[] {placeBlock, placeBlock.above(), placeBlock.below()};

                                    for (BlockPos placementPos : positionsToTry) {
                                        if (level.getBlockState(placementPos.below()).isSolid() && level.getBlockState(placementPos).canBeReplaced()) {
                                            double angle = Math.toDegrees(Math.atan2(placePos.z - player.getZ(), placePos.x - player.getX()) + Math.PI / 2);
                                            int rot = Integer.valueOf(RotationSegment.convertToSegment((float)angle));
                                            BlockState state = Blocks.OAK_SIGN.defaultBlockState().setValue(StandingSignBlock.ROTATION, rot);
                                            level.setBlock(placementPos, state, 2);
                                            placedAt = placementPos;
                                            break;
                                        }
                                    }
                                }
                            }

                            if (placedAt != null && level.getBlockEntity(placedAt) instanceof SignBlockEntity sign) {
                                WorldUtil.SignWriter writer = new WorldUtil.SignWriter(sign);
                                int selection = rand.nextInt(5);
                                if (selection == 0) {
                                    writer
                                            .setFrontLine(0, name)
                                            .setFrontLine(1, "was here")
                                            .write();
                                    bizarroOccurance = true;
                                } else if (selection == 1) {
                                    writer
                                            .setFrontLine(1, "Welcome back")
                                            .write();
                                } else if (selection == 2) {
                                    writer
                                            .setFrontLine(1, "We missed you")
                                            .write();
                                } else if (selection == 3) {
                                    writer
                                            .setFrontLine(0, "Please don't")
                                            .setFrontLine(1, "leave us again")
                                            .write();
                                } else if (selection == 4) {
                                    writer
                                            .setFrontLine(0, "Where have")
                                            .setFrontLine(1, "you been?")
                                            .write();
                                } else {
                                    throw new IllegalStateException("Invalid sign selection");
                                }
                            }
                        }
                    }

                    if (bizarroOccurance) {
                        data.bizarro.bizarroEncounters++;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    private static void handleCooldownTick(ServerTickEvent.Post event) {
        WorldData data = WorldData.get(event.getServer());
        if (data.bizarro.cooldown > 0) {
            data.bizarro.cooldown--;
        }
    }
}
