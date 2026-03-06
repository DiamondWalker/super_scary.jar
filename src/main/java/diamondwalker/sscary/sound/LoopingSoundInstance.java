package diamondwalker.sscary.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public abstract class LoopingSoundInstance extends AbstractTickableSoundInstance {
    protected LoopingSoundInstance(SoundEvent sound, SoundSource source) {
        super(sound, source, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
    }

    protected abstract boolean shouldContinue();

    @Override
    public void tick() {
        if (!shouldContinue()) stop();
    }
}
