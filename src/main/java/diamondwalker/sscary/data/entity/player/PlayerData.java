package diamondwalker.sscary.data.entity.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class PlayerData implements INBTSerializable<CompoundTag> {
    public boolean visageHealDisable = false;
    public int healFlashCooldown = 0;

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

    }
}
