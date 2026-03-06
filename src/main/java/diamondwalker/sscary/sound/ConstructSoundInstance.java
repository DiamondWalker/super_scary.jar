package diamondwalker.sscary.sound;

import diamondwalker.sscary.entity.entity.construct.EntityConstruct;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class ConstructSoundInstance extends LoopingSoundInstance {
    private final EntityConstruct construct;

    public ConstructSoundInstance(EntityConstruct construct) {
        super(SScarySounds.CONSTRUCT.value(), construct.getSoundSource());
        this.construct = construct;
        volume = 0.35f;
        relative = true;
    }

    @Override
    protected boolean shouldContinue() {
        return !construct.isRemoved() && construct.showAngeryEffects();
    }

    @Override
    public void tick() {
        super.tick();
        pitch = 0.4f + random.nextFloat() * 1.2f;
    }
}
