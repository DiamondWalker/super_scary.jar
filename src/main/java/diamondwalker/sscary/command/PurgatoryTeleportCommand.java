package diamondwalker.sscary.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import diamondwalker.sscary.registry.SScaryDimensions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class PurgatoryTeleportCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext context) {
        return Commands.literal("teleportToPurgatory")
                .executes(commandContext -> {
                            ServerPlayer sender = commandContext.getSource().getPlayer();
                            MinecraftServer minecraftserver = sender.level().getServer();
                            ResourceKey<Level> resourcekey = sender.level().dimension() == SScaryDimensions.PURGATORY_LEVEL_KEY ?
                                    Level.OVERWORLD : SScaryDimensions.PURGATORY_LEVEL_KEY;

                            ServerLevel portalDimension = minecraftserver.getLevel(resourcekey);
                            if (portalDimension != null && !sender.isPassenger()) {
                                sender.teleportTo(portalDimension, sender.getX(), sender.getY(), sender.getZ(), sender.getYRot(), sender.getXRot());
                            }

                            return 0;
                        }
                );
    }
}
