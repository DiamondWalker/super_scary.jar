package diamondwalker.twais.handler.event.random;

import diamondwalker.twais.data.server.WorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class HealthChangeHandler {
    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);

        if (!data.areEventsOnCooldown() && data.progression.hasBeenAngered()) {
            RandomSource random = server.overworld().getRandom();
            if (random.nextInt(86_000) == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (!isInDanger(player)) {
                        player.setHealth(Math.max(1, player.getMaxHealth() - player.getHealth()));
                        FoodData food = player.getFoodData();
                        if (food.getFoodLevel() > 10) {
                            food.setFoodLevel(10);
                            food.setSaturation(0.0f);
                            food.setExhaustion(0.0f);
                        }
                        data.eventCooldown();
                    }
                }
            }
        }
    }

    private static boolean isInDanger(Player player) {
        if (player.isDeadOrDying()) return true;

        if (player.isHurt() && player.getHealth() <= player.getMaxHealth() / 2) return true;

        if (player.isOnFire() || player.hurtTime > 0) return true;

        for (MobEffectInstance potionEffect : player.getActiveEffects()) {
            if (potionEffect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) return true;
        }

        for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(10))) {
            if (entity instanceof Targeting attacker && attacker.getTarget() == player) return true;
        }

        return false;
    }
}
