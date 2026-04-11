package diamondwalker.sscary.script.randomevent;

import diamondwalker.sscary.registry.SScaryMusic;
import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.registry.SScarySounds;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.script.ScriptType;
import diamondwalker.sscary.script.variable.IntegerVariable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundSource;
import org.joml.Vector3f;

import java.util.Random;

public class PartyScript extends Script {
    private static final int WACKY_COLOR_TICKS = 4;

    private final IntegerVariable time = IntegerVariable.create().save("ticks").sync().define(this);

    public PartyScript(int clientId) {
        super(SScaryScripts.PARTY.get(), clientId);
    }

    public PartyScript(MinecraftServer server) {
        super(SScaryScripts.PARTY.get(), server);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!clientSide) {
            chatMessageForAll(Component.literal("PARTY TIME!!!!"));
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.connection.send(new ClientboundSoundPacket(SScarySounds.PARTY_START, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 0.6F, server.overworld().getRandom().nextLong()));
            }
        }
    }

    @Override
    public void tick() {
        if (!clientSide) {
            if (time.get() >= 1040) {
                end();
            }
            time.set(time.get() + 1);
        }
    }

    @Override
    public void modifyLightmap(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {
        if (hasPartyStarted()) {
            Random thisTickRand = new Random(level.getGameTime() / WACKY_COLOR_TICKS * (pixelX + pixelY + 1));
            Random nextTickRand = new Random((level.getGameTime() / WACKY_COLOR_TICKS + 1) * (pixelX + pixelY + 1));

            float lerp = ((partialTicks + level.getGameTime()) % WACKY_COLOR_TICKS) / WACKY_COLOR_TICKS;

            Vector3f thisTickCol = new Vector3f(thisTickRand.nextFloat(), thisTickRand.nextFloat(), thisTickRand.nextFloat()).mul(thisTickRand.nextFloat());
            Vector3f nextTickCol = new Vector3f(nextTickRand.nextFloat(), nextTickRand.nextFloat(), nextTickRand.nextFloat()).mul(nextTickRand.nextFloat());
            Vector3f col = thisTickCol.lerp(nextTickCol, lerp);

            colors.set(col);
        }
    }

    @Override
    public Music getMusic() {
        return hasPartyStarted() ? SScaryMusic.PARTY : null;
    }

    private boolean hasPartyStarted() {
        return time.get() >= 40;
    }
}
