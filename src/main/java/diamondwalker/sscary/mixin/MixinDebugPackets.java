package diamondwalker.sscary.mixin;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.network.UpdateSyncedScriptPacket;
import diamondwalker.sscary.network.debug.PathRenderingPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.PathfindingDebugPayload;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(DebugPackets.class)
public class MixinDebugPackets {
    @Inject(method = "sendPathFindingPacket", at = @At("HEAD"))
    private static void sendPathFindingPacket(Level level, Mob mob, Path path, float maxDistanceToWaypoint, CallbackInfo ci) {
        if (SScary.DEV_MODE) {
            if (path != null) PacketDistributor.sendToAllPlayers(new PathRenderingPacket(mob.getId(), path, maxDistanceToWaypoint));

        }
    }
}
