package diamondwalker.sscary.network.debug;

import diamondwalker.sscary.SScary;
import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.NewScriptsClientData;
import diamondwalker.sscary.network.UpdateSyncedScriptPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record PathRenderingPacket(int entityId, Path path, float maxNodeDistance) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, PathRenderingPacket> STREAM_CODEC = CustomPacketPayload.codec(
            PathRenderingPacket::write, PathRenderingPacket::new
    );
    public static final CustomPacketPayload.Type<PathRenderingPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SScary.MODID, "path_rendering_packet"));


    private PathRenderingPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), readPath(buf), buf.readFloat());
    }

    private void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);

        writePath(buffer, path);

        //this.path.writeToStream(buffer);
        buffer.writeFloat(this.maxNodeDistance);
    }

    private static void writePath(FriendlyByteBuf buffer, Path path) {
        buffer.writeBoolean(path.canReach());
        buffer.writeInt(path.getNextNodeIndex());
        buffer.writeBlockPos(path.getTarget());
        List<Node> nodes = new ArrayList<>(path.getNodeCount());
        for (int i = 0; i < path.getNodeCount(); i++) nodes.add(path.getNode(i));
        buffer.writeCollection(nodes, (p_294084_, p_294085_) -> p_294085_.writeToStream(p_294084_));
    }

    private static Path readPath(FriendlyByteBuf buffer) {
        boolean flag = buffer.readBoolean();
        int nextNodeIndex = buffer.readInt();
        BlockPos blockpos = buffer.readBlockPos();
        List<Node> list = buffer.readList(Node::createFromStream);
        Path path = new Path(list, blockpos, flag);
        path.setNextNodeIndex(nextNodeIndex);
        return path;
    }

    @Override
    public CustomPacketPayload.Type<PathRenderingPacket> type() {
        return TYPE;
    }

    public static void handle(final PathRenderingPacket packet, final IPayloadContext context) {
        Minecraft.getInstance()
                .debugRenderer
                .pathfindingRenderer
                .addPath(packet.entityId(), packet.path(), packet.maxNodeDistance());
    }
}
