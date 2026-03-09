package diamondwalker.sscary.sound;

import diamondwalker.sscary.registry.SScarySounds;
import diamondwalker.sscary.script.CalculationScript;
import net.minecraft.sounds.SoundSource;

public class CalculationSoundInstance extends LoopingSoundInstance {
    private final CalculationScript script;

    public CalculationSoundInstance(CalculationScript script) {
        super(SScarySounds.CALCULATION_PUNISHMENT.value(), SoundSource.MASTER);
        this.script = script;
        volume = 0.8f;
        pitch = 0.7f + random.nextFloat() * 0.6f;
        relative = true;
    }

    @Override
    protected boolean shouldContinue() {
        return !script.hasEnded() && script.state.get() == CalculationScript.CalculationState.PUNISHMENT;
    }
}
