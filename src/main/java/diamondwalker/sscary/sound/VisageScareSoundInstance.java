package diamondwalker.sscary.sound;

import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class VisageScareSoundInstance extends AbstractTickableSoundInstance { // TODO: this is currently unused because I have no idea how to make it work
    private final float volumeIncreaseRate;
    private final float maxVolume;

    public VisageScareSoundInstance(float volumeIncreaseRate, float maxVolume) {
        super(SScarySounds.VISAGE_CHASE.value(), SoundSource.MASTER, SoundInstance.createUnseededRandom());
        this.volumeIncreaseRate = volumeIncreaseRate;
        this.maxVolume = maxVolume;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0f;
        relative = true;
    }

    @Override
    public void tick() {
        this.volume += volumeIncreaseRate;
        if (volume >= maxVolume) {
            this.stop();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}
