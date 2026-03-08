package diamondwalker.sscary.randomevent.common;

import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class LightningEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerLevel level = player.serverLevel();
            if (level.dimension() == Level.OVERWORLD) {
                RandomSource random = player.getRandom();
                if (!level.isThundering()) {
                    BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, player.blockPosition());
                    if (pos.getY() <= player.getBlockY()) {
                        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(player.level());
                        if (lightningbolt != null) {
                            lightningbolt.moveTo(Vec3.atBottomCenterOf(pos));
                            lightningbolt.setVisualOnly(false);
                            level.addFreshEntity(lightningbolt);
                        }
                    }
                } else {
                    ScriptBuilder script = new ScriptBuilder(server);
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

        return true;
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.COMMON;
    }
}
