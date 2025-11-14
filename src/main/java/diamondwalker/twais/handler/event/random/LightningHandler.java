package diamondwalker.twais.handler.event.random;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.ScriptBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class LightningHandler {
    @SubscribeEvent
    public static void handleServerTick(ServerTickEvent.Post event) {
        if (!WorldData.get(event.getServer()).areEventsOnCooldown()) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                ServerLevel level = player.serverLevel();
                if (level.dimension() == Level.OVERWORLD) {
                    RandomSource random = player.getRandom();
                    if (random.nextInt(50_000) == 0) {
                        if (!level.isThundering()) {
                            BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, player.blockPosition());
                            if (pos.getY() <= player.getBlockY()) {
                                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(player.level());
                                if (lightningbolt != null) {
                                    lightningbolt.moveTo(Vec3.atBottomCenterOf(pos));
                                    lightningbolt.setVisualOnly(false);
                                    level.addFreshEntity(lightningbolt);
                                    WorldData.get(event.getServer()).eventCooldown();
                                }
                            }
                        } else {
                            ScriptBuilder script = new ScriptBuilder(event.getServer());
                            for (int i = 0; i < 30; i++) {
                                script.action((serv) -> {
                                    while (random.nextInt(15) > 0) {
                                        int x = player.getBlockX() - 80 + random.nextInt(161);
                                        int z = player.getBlockZ() - 80 + random.nextInt(161);
                                        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

                                        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(player.level());
                                        if (lightningbolt != null) {
                                            lightningbolt.moveTo(Vec3.atBottomCenterOf(new BlockPos(x, y, z)));
                                            lightningbolt.setVisualOnly(false);
                                            level.addFreshEntity(lightningbolt);
                                        }
                                    }
                                });
                                script.rest(1);
                            }
                            script.startScript();
                        }
                    }
                }
            }
        }
    }
}
