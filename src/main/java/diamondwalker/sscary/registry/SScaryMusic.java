package diamondwalker.sscary.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.Music;

public class SScaryMusic {
    public static final Music PARTY = new Music(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SScarySounds.PARTY.value()), 0, 0, true);
}
