package diamondwalker.twais.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.Music;

public class TWAISMusic {
    public static final Music PARTY = new Music(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(TWAISSounds.PARTY.value()), 0, 0, true);
}
