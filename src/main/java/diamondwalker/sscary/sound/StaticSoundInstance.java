package diamondwalker.sscary.sound;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.data.client.StaticData;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class StaticSoundInstance extends LoopingSoundInstance {
    public StaticSoundInstance() {
        super(SScarySounds.STATIC.value(), SoundSource.MASTER);
        volume = 0.5f;
        relative = true;
    }

    @Override
    protected boolean shouldContinue() {
        StaticData data = ClientData.get().staticData;
        return data != null && data.shouldPlaySound();
    }
}
