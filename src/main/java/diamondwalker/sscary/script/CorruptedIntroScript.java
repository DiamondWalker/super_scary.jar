package diamondwalker.sscary.script;

import diamondwalker.sscary.data.PermanentSaveData;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.script.variable.BlockPosVariable;
import diamondwalker.sscary.script.variable.BooleanVariable;
import diamondwalker.sscary.script.variable.IntegerVariable;
import diamondwalker.sscary.script.variable.StringVariable;
import diamondwalker.sscary.util.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CorruptedIntroScript extends Script {
    private final BlockPosVariable origin = BlockPosVariable.create().save("origin").define(this);
    private final IntegerVariable blocksBroken = IntegerVariable.create().save("blocksBroken").define(this);
    private final BooleanVariable triggered = BooleanVariable.create().save("triggered").define(this);

    private final BooleanVariable friendIntervened = BooleanVariable.create().save("friendIntervention").define(this);
    private final IntegerVariable time = IntegerVariable.create().save("time").define(this);
    private final BooleanVariable silenced = BooleanVariable.create().save("silenced").sync().define(this);
    private final StringVariable playerName = StringVariable.create().save("playerName").define(this);

    private final BooleanVariable hello = BooleanVariable.create().save("hello").define(this);

    private final BooleanVariable sorry = BooleanVariable.create().save("sorry").define(this);
    private final IntegerVariable sorryTime = IntegerVariable.create().save("sorryTime").define(this);

    private final BooleanVariable calledOutForRepairing = BooleanVariable.create().save("calledOutForRepairing").define(this);
    private final IntegerVariable repairCalloutTime = IntegerVariable.create().save("repairCalloutTime").define(this);

    private final IntegerVariable talkCount = IntegerVariable.create().save("talkCount").define(this);

    private final IntegerVariable normalShutUp = IntegerVariable.create().save("normalShutUp").define(this);

    private final IntegerVariable friendShutUp = IntegerVariable.create().save("friendShutUp").define(this);
    private final StringVariable friendShutUpPlayerName = StringVariable.create().save("friendShutUpPlayerName").define(this);

    public CorruptedIntroScript(MinecraftServer server, BlockPos origin) {
        this(server);
        this.origin.set(origin);
    }

    public CorruptedIntroScript(MinecraftServer server) {
        super(SScaryScripts.CORRUPTED_INTRO.get(), server);
    }

    public CorruptedIntroScript(int clientId) {
        super(SScaryScripts.CORRUPTED_INTRO.get(), clientId);
    }

    @Override
    public void tick() {
        if (clientSide) {
            if (silenced.get() && Minecraft.getInstance().screen instanceof ChatScreen) {
                Minecraft.getInstance().setScreen(null);
            }

            return;
        }

        if (WorldData.get(server).progression.hasBeenAngered()) {
            end();
        }

        if (triggered.get()) {
            if (friendIntervened.get()) {
                if (friendShutUp.get() > 0) {
                    if (friendShutUp.get() == 40) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Quiet, " + friendShutUpPlayerName.get() + ". The adults are speaking."));
                        silenced.set(true);
                    }
                    friendShutUp.set(friendShutUp.get() - 1);
                    return;
                }

                handleCorruptedFriendMonologue(time.get());
            } else {
                if (sorryTime.get() > 0) {
                    if (sorryTime.get() == 40) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "yeah well sorry aint gonna cut it pal"));
                    }
                    sorryTime.set(sorryTime.get() - 1);
                    return;
                }

                if (normalShutUp.get() > 0) {
                    if (normalShutUp.get() == 40) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "shut"));
                        silenced.set(true);
                    }
                    normalShutUp.set(normalShutUp.get() - 1);
                    return;
                }

                if (repairCalloutTime.get() > 0) {
                    if (repairCalloutTime.get() == 80) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "nonononono dont just put it back and pretend nothing happened"));
                    } else if (repairCalloutTime.get() == 40) {
                        chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "You know what you did"));
                    }
                    repairCalloutTime.set(repairCalloutTime.get() - 1);
                    return;
                }

                handleCorruptedMonologue(time.get());
            }

            time.set(time.get() + 1);
        }
    }

    @Override
    public void handleBlockPlace(ServerPlayer breaker, BlockState state, BlockPos blockPos) {
        if (state.is(Blocks.COBBLESTONE) && triggered.get()) {
            if (isBuildAt(blockPos)) {
                blocksBroken.set(blocksBroken.get() - 1);
                if (blocksBroken.get() <= 1) {
                    if (!calledOutForRepairing.get()) {
                        calledOutForRepairing.set(true);
                        repairCalloutTime.set(120);
                        time.set(391);
                    }
                }
            }
        }
    }

    @Override
    public void handleBlockBreak(ServerPlayer breaker, BlockState state, BlockPos blockPos) {
        if (state.is(Blocks.COBBLESTONE)) {
            if (isBuildAt(blockPos)) {
                if (!triggered.get() && blocksBroken.get() >= 7) {
                    WorldData data = WorldData.get(server);

                    if (data.newScripts.getScripts().stream().noneMatch(s -> (s instanceof CorruptedIntroScript c && c.triggered.get()))) { // make sure the event isn't already active
                        triggered.set(true);
                        playerName.set(breaker.getDisplayName().getString());
                        friendIntervened.set(data.friend.friendJoined && !data.friend.friendLeft);
                        data.scripts.lock("corrupted_entity");
                        if (friendIntervened.get()) data.scripts.lock("friend");
                    }
                }
                blocksBroken.set(blocksBroken.get() + 1);
            }
        }
    }

    @Override
    public void handleChatInput(ServerPlayer sender, String message) {
        if (time.get() < 250 && !hello.get()) { // between yo and wtf
            if (message.toLowerCase().matches("(.+)?(hi|hey|hello)(.+)?")) {
                hello.set(true);
                time.set(151); // this both ensures we skip the first "yo" if the player spoke first
            }
        }

        if (friendIntervened.get()) {
            if (friendShutUp.get() <= 0) {
                if (time.get() >= 1510 && time.get() <= 1740) {
                    friendShutUp.set(80);
                    friendShutUpPlayerName.set(sender.getGameProfile().getName());
                }
            }
        } else {
            if (time.get() > 390 && message.toLowerCase().matches("(.+)?(sorry|apologies|apologize)(.+)?") && !sorry.get()) {
                sorry.set(true);
                sorryTime.set(80);
            } else {
                talkCount.set(talkCount.get() + 1);
                if (talkCount.get() >= 3) {
                    normalShutUp.set(80);
                    sorryTime.set(0); // just in case sorry was still ongoing
                }
            }
        }
    }

    @Override
    public void onEnd() {
        if (!clientSide) {
            WorldData data = WorldData.get(server);
            data.scripts.unlock("corrupted_entity");
            if (friendIntervened.get()) data.scripts.unlock("friend");
        }
    }

    private boolean isBuildAt(BlockPos pos) {
        if (Math.abs(pos.getX() - origin.get().getX()) <= 12 && Math.abs(pos.getZ() - origin.get().getZ()) <= 12 && pos.getY() >= origin.get().getY() && pos.getY() - origin.get().getY() <= 12) {
            return true;
        }
        return false;
    }

    private void handleCorruptedMonologue(int time) {
        switch (time) {
            case 0: {
                sendJoinMessage(ChatUtil.CORRUPTED_ENTITY_NAME);
                break;
            }
            case 150: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "yo"));
                break;
            }
            case 250: {
                if (hello.get()) {
                    chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "yes hi wtf are you doing?"));
                } else {
                    chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "wtf do you think youre doing?"));
                }
                break;
            }
            case 330: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my box?"));
                break;
            }
            case 390: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my freaking box bro???"));
                break;
            }
            case 440: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "do you have any idea how long that took me to make?"));
                break;
            }
            case 510: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "I spend my blood, sweat, and tears trying to create something BEAUTIFUL"));
                break;
            }
            case 570: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "and then some ignorant ASSHOLE with NO appreciation for modernist architecture shows up and ruins it"));
                break;
            }
            case 640: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "you have destroyed my work"));
                break;
            }
            case 680: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "you have destroyed my LIFE"));
                break;
            }
            case 740: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "So now I am gonna make yours a living hell"));
                break;
            }
            case 830: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "Do you even know who I am?"));
                break;
            }
            case 880: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "I know people"));
                break;
            }
            case 920: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "powerful people"));
                break;
            }
            case 960: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "dangerous people"));
                break;
            }
            case 1020: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "they aren't people"));
                break;
            }
            case 1070: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "they're weird eldritch void entity things idfk"));
                break;
            }
            case 1130: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "But theyre gonna mess you up real bad for what you just did."));
                break;
            }
            case 1180: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "y'all better sleep with one eye open.").withStyle(ChatFormatting.DARK_RED));
                WorldData.get(server).progression.startAnger(server);
                PermanentSaveData data = PermanentSaveData.getOrCreateInstance();
                data.setCorruptedAngered(true);
                data.saveChanges();
                end();
                break;
            }
        }
    }

    private void handleCorruptedFriendMonologue(int time) {
        switch (time) {
            case 0: {
                sendJoinMessage(ChatUtil.CORRUPTED_ENTITY_NAME);
                break;
            }
            case 150: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "yo"));
                break;
            }
            case 250: {
                if (hello.get()) {
                    chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "yes hi wtf are you doing?"));
                } else {
                    chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "wtf do you think youre doing?"));
                }
                break;
            }
            case 330: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my box?"));
            break;
            }
            case 390: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "are you breaking my freaking box bro???"));
                break;
            }
            case 410: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Excuse me, what seems to be the problem here?"));
                break;
            }
            case 490: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "who the hell are you???"));
                break;
            }
            case 590: {
                String possessive = !playerName.get().endsWith("s") ? playerName.get() + "'s" : "'";
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "I am " + possessive + " companion. If you have a problem with them, you have a problem with ME."));
                break;
            }
            case 790: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "lol are you their boyfriend or sum?"));
                break;
            }
            case 860: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "No."));
                break;
            }
            case 920: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Believe it or not, I actually have standards."));
                break;
            }
            case 1100: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "k"));
                break;
            }
            case 1150: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "so look how bout this"));
                break;
            }
            case 1190: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "I give you some cash and you piss off and let me murder " + playerName.get().toLowerCase() + " in peace."));
                break;
            }
            case 1290: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "hows 30 bucks sound?"));
                break;
            }
            case 1360: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Don't be absurd."));
                break;
            }
            case 1410: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "I would not sell my friend's life for thirty dollars. That would be preposterous."));
                break;
            }
            case 1510: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "...fifty."));
                break;
            }
            case 1550: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "40"));
                break;
            }
            case 1590: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "You drive a hard bargain, kid."));
                break;
            }
            case 1630: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Forty-five. Best offer."));
                break;
            }
            case 1690: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "deal"));
                break;
            }
            case 1740: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Pleasure doing business with you."));
                break;
            }
            case 1800: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.FRIEND_NAME, "Farewell, " + playerName.get() + ". I hope your death is quick and painless."));
                break;
            }
            case 1840: {
                chatMessageForAll(ChatUtil.getLeaveMessage("Friend"));
                break;
            }
            case 1940: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "...\"quick and painless\", huh?"));
                break;
            }
            case 2000: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "nah"));
                break;
            }
            case 2050: {
                chatMessageForAll(ChatUtil.getEntityChatMessage(ChatUtil.CORRUPTED_ENTITY_NAME, "i'm gonna have some fun with you.").withStyle(ChatFormatting.DARK_RED));

                WorldData data = WorldData.get(server);
                data.friend.friendLeft = true;
                data.progression.startAnger(server);
                PermanentSaveData permanentData = PermanentSaveData.getOrCreateInstance();
                permanentData.setCorruptedAngered(true);
                permanentData.saveChanges();
                end();
                break;
            }
        }
    }
}
