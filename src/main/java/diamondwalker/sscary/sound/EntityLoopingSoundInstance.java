package diamondwalker.sscary.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public abstract class EntityLoopingSoundInstance<T extends Entity> extends LoopingSoundInstance {
    protected final T entity;

    public EntityLoopingSoundInstance(T entity, SoundEvent p_235076_) {
        super(p_235076_, entity.getSoundSource());
        this.entity = entity;
    }

    @Override
    protected boolean shouldContinue() {
        return !entity.isRemoved();
    }

    @Override
    public void tick() {
        super.tick();
        this.x = (float)entity.getX();
        this.y = (float)entity.getY();
        this.z = (float)entity.getZ();
    }
}
