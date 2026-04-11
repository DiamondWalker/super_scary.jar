package diamondwalker.sscary.script.randomevent;

import diamondwalker.sscary.registry.SScaryScripts;
import diamondwalker.sscary.registry.SScarySounds;
import diamondwalker.sscary.script.Script;
import diamondwalker.sscary.script.ScriptType;
import diamondwalker.sscary.script.variable.IntegerVariable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import org.joml.Vector3f;

public class DarkWorldScript extends Script {
    private final IntegerVariable time = IntegerVariable.create().save("ticks").define(this);

    public DarkWorldScript(MinecraftServer server) {
        super(SScaryScripts.DARK_WORLD.get(), server);
    }

    public DarkWorldScript(int clientId) {
        super(SScaryScripts.DARK_WORLD.get(), clientId);
    }

    @Override
    public void onStart() {
        if (!clientSide) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.connection.send(new ClientboundSoundPacket(SScarySounds.WORLD_DARKEN, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 0.6F, random.nextLong()));
            }
            time.set(20 * (5 + random.nextInt(15)));
        }
    }

    @Override
    public void tick() {
        if (!clientSide) {
            if (time.get() > 0) {
                time.set(time.get() - 1);
            } else {
                end();
            }
        }
    }

    @Override
    public void modifyLightmap(ClientLevel level, float partialTicks, float skyDarken, float blockLightRedFlicker, float skyLight, int pixelX, int pixelY, Vector3f colors) {
        colors.set(0.0f, 0.0f, 0.0f);
    }
}
