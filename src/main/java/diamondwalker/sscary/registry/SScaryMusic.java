package diamondwalker.sscary.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.Music;

public class SScaryMusic {
    public static final Music PARTY = new Music(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SScarySounds.PARTY.value()), 0, 0, true);
    public static final Music FRIED_STEVE = new Music(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SScarySounds.FRIED_STEVE_MUSIC.value()), 0, 0, true);
    public static final Music FRIED_STEVE_PRELUDE = new Music(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SScarySounds.FRIED_STEVE_PRELUDE.value()), 0, 0, true); // TODO: update this so it loops better
}
