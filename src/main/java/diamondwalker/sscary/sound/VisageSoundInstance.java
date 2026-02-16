package diamondwalker.sscary.sound;

import diamondwalker.sscary.entity.visage.EntityVisage;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class VisageSoundInstance extends AbstractTickableSoundInstance {
    private final EntityVisage visage;

    public VisageSoundInstance(EntityVisage visage) {
        super(SScarySounds.VISAGE_CHASE.value(), visage.getSoundSource(), SoundInstance.createUnseededRandom());
        this.visage = visage;
        this.looping = true;
        this.delay = 0;
        this.volume = 2.0f;
    }

    @Override
    public void tick() {
        if (!visage.isRemoved()) {
            this.x = (float)visage.getX();
            this.y = (float)visage.getY();
            this.z = (float)visage.getZ();
            /*float f = (float)visage.getDeltaMovement().horizontalDistance();
            if (f >= 0.01F) {
                this.pitch = Mth.lerp(Mth.clamp(f, MIN_PITCH, MAX_PITCH), MIN_PITCH, MAX_PITCH);
                this.volume = Mth.lerp(Mth.clamp(f, 0.0F, 1.0F), 0.0F, 2.0F);
            } else {
                this.pitch = 0.0F;
                this.volume = 0.0F;
            }*/
        } else {
            stop();
        }
    }
}
