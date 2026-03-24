package diamondwalker.sscary.sound;

import diamondwalker.sscary.gui.screen.VisageDeathScreen;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class VisageDeathStaticSoundInstance extends LoopingSoundInstance {
    private final VisageDeathScreen screen;

    public VisageDeathStaticSoundInstance(VisageDeathScreen screen) {
        super(SScarySounds.STATIC.value(), SoundSource.MASTER);
        volume = 0.5f;
        relative = true;
        this.screen = screen;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isStopped()) {
            volume = screen.playStaticSound() ? 0.5f : 0.0f;
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    protected boolean shouldContinue() {
        return Minecraft.getInstance().screen == screen;
    }
}
