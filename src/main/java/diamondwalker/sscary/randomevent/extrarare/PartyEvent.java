package diamondwalker.sscary.randomevent.extrarare;

import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.randomevent.EnumEventRarity;
import diamondwalker.sscary.randomevent.RandomEvent;
import diamondwalker.sscary.registry.SScarySounds;
import diamondwalker.sscary.script.randomevent.FriedSteveScript;
import diamondwalker.sscary.script.randomevent.PartyScript;
import diamondwalker.sscary.util.ScriptBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.PacketDistributor;

public class PartyEvent extends RandomEvent {
    public boolean execute(MinecraftServer server, ServerPlayer[] validPlayers) {
        return WorldData.get(server).newScripts.startScript(new PartyScript(server));
    }

    @Override
    public EnumEventRarity getRarity() {
        return EnumEventRarity.EXTRA_RARE;
    }
}
