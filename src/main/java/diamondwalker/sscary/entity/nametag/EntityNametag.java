package diamondwalker.sscary.entity.nametag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidType;

public class EntityNametag extends Entity {
    private int time;

    public EntityNametag(EntityType<?> entityType, Level level) {
        super(entityType, level);
        time = random.nextInt(4000) + 3600;
        noPhysics = true;
    }

    /*public EntityNametag(Level level) {
        this(TWAISEntities.NAMETAG.get(), level);
    }*/

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            time--;
            if (time <= 0) discard();
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("TimeLeft")) time = compound.getInt("TimeLeft");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("TimeLeft", time);
    }
}
