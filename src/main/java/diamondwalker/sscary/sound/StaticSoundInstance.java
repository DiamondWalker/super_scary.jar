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

public class StaticSoundInstance extends AbstractTickableSoundInstance {
    public StaticSoundInstance() {
        super(SScarySounds.STATIC.value(), SoundSource.MASTER, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
    }

    @Override
    public void tick() {
        StaticData data = ClientData.get().staticData;
        if (data == null || !data.shouldPlaySound()) {
            this.stop();
        }
    }
}
