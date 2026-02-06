package diamondwalker.sscary.data.entity.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class PlayerData implements INBTSerializable<CompoundTag> {
    public boolean visageHealDisable = false;
    public int healFlashCooldown = 0;

    public int deathCounter = 0;
    public boolean eternalPurgatory = false;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Deaths", deathCounter);
        tag.putBoolean("EternalPurgatory", eternalPurgatory);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        deathCounter = nbt.getInt("Deaths");
        eternalPurgatory = nbt.getBoolean("EternalPurgatory");
    }
}
