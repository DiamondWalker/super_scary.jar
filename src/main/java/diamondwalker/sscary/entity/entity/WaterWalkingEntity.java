package diamondwalker.sscary.entity.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.shapes.CollisionContext;

public class WaterWalkingEntity extends PathfinderMob {
    protected WaterWalkingEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        if (this.canWalkOnWater()) this.setPathfindingMalus(PathType.WATER, 0.0F);
        if (this.canWalkOnLava()) this.setPathfindingMalus(PathType.LAVA, 0.0F);
    }

    public boolean canWalkOnWater() {
        return true;
    }

    public boolean canWalkOnLava() {
        return true;
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        if (super.canStandOnFluid(fluidState)) return true;
        if (canWalkOnWater() && fluidState.is(FluidTags.WATER)) return true;
        if (canWalkOnLava() && fluidState.is(FluidTags.LAVA)) return true;
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (isInWater() && canWalkOnWater()) walkOn(FluidTags.WATER);
        if (isInLava() && canWalkOnLava()) walkOn(FluidTags.LAVA);
    }

    private void walkOn(TagKey<Fluid> fluid) {
        CollisionContext collisioncontext = CollisionContext.of(this);
        if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true)
                && !this.level().getFluidState(this.blockPosition().above()).is(fluid)) {
            this.setOnGround(true);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5).add(0.0, 0.05, 0.0));
        }
    }

    static class WaterWalkingPathNavigation extends GroundPathNavigation {
        WaterWalkingPathNavigation(WaterWalkingEntity entity, Level level) {
            super(entity, level);
        }

        @Override
        protected boolean hasValidPathType(PathType pathType) {
            WaterWalkingEntity entity = (WaterWalkingEntity) mob;
            if (entity.canWalkOnWater() && pathType == PathType.WATER) return true;
            if (entity.canWalkOnLava() && pathType == PathType.LAVA) return true;
            return super.hasValidPathType(pathType);
        }

        @Override
        public boolean isStableDestination(BlockPos pos) {
            WaterWalkingEntity entity = (WaterWalkingEntity) mob;
            if (entity.canWalkOnWater() && this.level.getBlockState(pos).is(Blocks.WATER)) return true;
            if (entity.canWalkOnLava() && this.level.getBlockState(pos).is(Blocks.LAVA)) return true;
            return super.isStableDestination(pos);
        }
    }
}
