package diamondwalker.sscary.sound;

import diamondwalker.sscary.entity.entity.construct.EntityConstruct;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class ConstructSoundInstance extends EntityLoopingSoundInstance<EntityConstruct> {
    public ConstructSoundInstance(EntityConstruct entity) {
        super(entity, SScarySounds.CONSTRUCT.value());
    }

    @Override
    protected boolean shouldContinue() {
        return super.shouldContinue() && entity.showAngeryEffects();
    }
}
