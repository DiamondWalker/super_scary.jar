package diamondwalker.sscary.sound;

import diamondwalker.sscary.entity.entity.visage.EntityVisage;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;

public class VisageSoundInstance extends EntityLoopingSoundInstance<EntityVisage> {
    public VisageSoundInstance(EntityVisage visage) {
        super(visage, SScarySounds.VISAGE_CHASE.value());
        this.volume = 2.0f;
    }
}
