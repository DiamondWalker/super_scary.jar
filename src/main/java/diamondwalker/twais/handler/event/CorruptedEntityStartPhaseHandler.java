package diamondwalker.twais.handler.event;

import diamondwalker.twais.data.server.WorldData;
import diamondwalker.twais.util.ChatUtil;
import diamondwalker.twais.util.ScriptBuilder;
import diamondwalker.twais.util.WorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class CorruptedEntityStartPhaseHandler {
    private static int friendState = 0; // 0 = nothing, 1 = he'll tell you to shut up, 2 = he already told you to shut up

    @SubscribeEvent
    private static void handleServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        WorldData data = WorldData.get(server);
        long time = data.progression.getTimeInWorld();
        if (!data.progression.hasBeenAngered() && time >= 72_000L && time % 24_000L == 0 && !data.scripts.hasLock("corrupted_entity")) {
            ServerLevel level = server.overworld();
            List<LevelChunk> possibleChunks = WorldUtil.getBuildableChunks(level);
            if (possibleChunks.isEmpty()) return;

            if (!possibleChunks.isEmpty()) {
                LevelChunk chosenChunk = possibleChunks.get(level.getRandom().nextInt(possibleChunks.size()));
                ChunkPos chosenPos = chosenChunk.getPos();
                int x = chosenPos.getMinBlockX() + level.random.nextInt(16);
                int z = chosenPos.getMinBlockZ() + level.random.nextInt(16);
                int y = Integer.MIN_VALUE;

                int minX = x - 12;
                int maxX = x + 12;
                int minZ = z - 12;
                int maxZ = z + 12;

                for (int cx = minX; cx <= maxX; cx++) {
                    for (int cz = minZ; cz <= maxZ; cz++) {
                        y = Math.max(y, level.getHeight(Heightmap.Types.MOTION_BLOCKING, cx, cz));
                    }
                }

                if (y < level.getMaxBuildHeight() - 15) {
                    for (int cx = minX; cx <= maxX; cx++) {
                        for (int cz = minZ; cz <= maxZ; cz++) {
                            for (int cy = 0; cy <= 12; cy++) {
                                level.setBlock(new BlockPos(cx, y + cy, cz), Blocks.COBBLESTONE.defaultBlockState(), 2);
                            }
                        }
                    }

                    WorldData.get(server).corruptedEntityBuilds.addBuild(new BlockPos(x, y, z));
                }
            }
        }
    }

    @SubscribeEvent
    private static void handlePlayerBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().is(Blocks.COBBLESTONE) && event.getPlayer() instanceof ServerPlayer player) {
            WorldData data = WorldData.get(player.getServer());
            if (data.corruptedEntityBuilds.isBuildAt(event.getPos())) {
                if (data.corruptedEntityBuilds.anger++ >= 7) {
                    if (data.friend.friendJoined && !data.friend.friendLeft) {
                        /*if (!data.friend.friendDislikesYou) {
                            buildFriendLoveCutsceneSequence(player);
                        } else {
                            buildFriendDislikeCutsceneSequence(player);
                        }*/
                        buildFriendDislikeCutsceneSequence(player);
                    } else {
                        buildNoFriendCutsceneSequence(player);
                    }
                }
            }
        }
    }

    /*private static void buildFriendLoveCutsceneSequence(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        String name = player.getDisplayName().getString();
        String nameLowercase = name.toLowerCase();
        String possessive = !name.endsWith("s") ? name + "'s" : "'";

        new ScriptBuilder(server, "corrupted_entity", "friend")
                .chatMessageForAll(ChatUtil.getJoinMessage(ChatUtil.CORRUPTED_ENTITY_NAME))
                .rest(150)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "yo"))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "wtf do you think youre doing?"))
                .rest(80)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my box?"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my freaking box bro???"))
                .rest(20)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Excuse me, what seems to be the problem here?"))
                .rest(80)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "who the hell are you???"))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "I am " + possessive + " companion. If you have a problem with them, you have a problem with ME."))
                .rest(200)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "lol are you their boyfriend or sum?"))
                .rest(70)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "No."))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Unfortunately, the relationship between " + name + " and I is purely platonic."))
                .rest(180)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "...\"unfortunately\", huh?"))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "must be hard"))
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "living in the friendzone"))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "Good thing you won't be living for long."))
                .rest(30)
                .chatMessageForAll(Component.literal("Friend was slain by c0rrup1e3_en1i1y"))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "now as i was saying"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, nameLowercase))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "You broke my shit"))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "so now me and my homies are gonna break YOU").withStyle(ChatFormatting.DARK_RED))
                .action((serv) -> WorldData.get(serv).friend.friendLeft = true)
                .action((serv) -> WorldData.get(serv).progression.startAnger(serv))
                .startScript();
    }*/

    private static void buildFriendDislikeCutsceneSequence(ServerPlayer player) {
        MinecraftServer server = player.getServer();

        String name = player.getDisplayName().getString();
        String nameLowercase = name.toLowerCase();
        String possessive = !name.endsWith("s") ? name + "'s" : "'";

        new ScriptBuilder(server, "corrupted_entity", "friend")
                .chatMessageForAll(ChatUtil.getJoinMessage(ChatUtil.CORRUPTED_ENTITY_NAME))
                .rest(150)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "yo"))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "wtf do you think youre doing?"))
                .rest(80)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my box?"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my freaking box bro???"))
                .rest(20)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Excuse me, what seems to be the problem here?"))
                .rest(80)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "who the hell are you???"))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "I am " + possessive + " companion. If you have a problem with them, you have a problem with ME."))
                .rest(200)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "lol are you their boyfriend or sum?"))
                .rest(70)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "No."))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Believe it or not, I actually have standards."))
                .rest(180)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "k"))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "so look how bout this"))
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "I give you some cash and you piss off and let me murder " + nameLowercase + " in peace."))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "hows 30 bucks sound?"))
                .rest(70)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Don't be absurd."))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "I would not sell my friend's life for thirty dollars. That would be preposterous."))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "...fifty."))
                .action((serv) -> friendState = 1)
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "40"))
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "You drive a hard bargain, kid."))
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Forty-five. Best offer."))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "deal"))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Pleasure doing business with you."))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Farewell, " + name + ". I hope your death is quick and painless."))
                .rest(40)
                .chatMessageForAll(ChatUtil.getLeaveMessage("Friend"))
                .action((serv) -> friendState = 0)
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "...\"quick and painless\", huh?"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "nah"))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "i'm gonna have some fun with you.").withStyle(ChatFormatting.DARK_RED))
                .action((serv) -> WorldData.get(serv).friend.friendLeft = true)
                .action((serv) -> WorldData.get(serv).progression.startAnger(serv))
                .startScript();
    }

    private static void buildNoFriendCutsceneSequence(ServerPlayer player) {
        MinecraftServer server = player.getServer();

        new ScriptBuilder(server, "corrupted_entity")
                .chatMessageForAll(ChatUtil.getJoinMessage(ChatUtil.CORRUPTED_ENTITY_NAME))
                .rest(150)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "yo"))
                .rest(100)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "wtf do you think youre doing?"))
                .rest(80)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my box?"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my freaking box bro???"))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "do you have any idea how long that took me to make?"))
                .rest(70)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "I spend my blood, sweat, and tears trying to create something BEAUTIFUL"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "and then some ignorant ASSHOLE with NO appreciation for modernist architecture shows up and ruins it"))
                .rest(70)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "you have destroyed my work"))
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "you have destroyed my LIFE"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "So now I am gonna make yours a living hell"))
                .rest(90)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "Do you even know who I am?"))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "I know people"))
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "powerful people"))
                .rest(40)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "dangerous people"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "they aren't people"))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "they're weird eldritch void entity things idfk"))
                .rest(60)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "But theyre gonna mess you up real bad for what you just did."))
                .rest(50)
                .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "y'all better sleep with one eye open.").withStyle(ChatFormatting.DARK_RED))
                .action((serv) -> WorldData.get(serv).progression.startAnger(serv))
                .startScript();
    }

    @SubscribeEvent
    private static void handlePlayerChat(ServerChatEvent event) {
        if (friendState == 1) {
            MinecraftServer server = event.getPlayer().getServer();

            new ScriptBuilder(server)
                    .chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Quiet, " + event.getUsername() + ". The adults are speaking."))
                    .action((serv) -> friendState = 2)
                    .startScript();

        }
    }
}
