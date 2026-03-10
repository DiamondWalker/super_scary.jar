package diamondwalker.sscary.sound;

import diamondwalker.sscary.data.client.ClientData;
import diamondwalker.sscary.entity.entity.friedsteve.EntityFriedSteve;
import diamondwalker.sscary.entity.entity.friedsteve.EnumFriedSteveState;
import diamondwalker.sscary.registry.SScarySounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class FriedSteveJumpscareSoundInstance extends LoopingSoundInstance {
    public FriedSteveJumpscareSoundInstance() {
        super(SScarySounds.FRIED_STEVE_JUMPSCARE.value(), SoundSource.HOSTILE);
        volume = 0.1f;
    }

    @Override
    protected boolean shouldContinue() {
        EntityFriedSteve steve = ClientData.get().friedSteve;
        return steve != null && !steve.isRemoved() && steve.getState().isPartOfJumpscare;
    }
}
