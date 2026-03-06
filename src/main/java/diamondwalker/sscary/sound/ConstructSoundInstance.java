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
        relative = true;
    }

    @Override
    protected boolean shouldContinue() {
        return !construct.isRemoved() && construct.showAngeryEffects();
    }
}
