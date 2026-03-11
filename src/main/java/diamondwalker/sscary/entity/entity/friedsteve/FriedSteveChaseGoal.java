package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.ai.ImprovedMeleeAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class FriedSteveChaseGoal extends ImprovedMeleeAttackGoal {
    private final EntityFriedSteve steve;

    public FriedSteveChaseGoal(EntityFriedSteve mob, double speedModifier) {
        super(mob, speedModifier, true);
        steve = mob;
    }

    @Override
    public boolean canUse() {
        return steve.getState() == EnumFriedSteveState.CHASING && super.canUse();
    }

    @Override
    public void tick() {
        super.tick();

        if (steve.isInFluidType()) {
            LivingEntity target = steve.getTarget();
            Vec3 relative3d = new Vec3(target.getX() - steve.getX(), 0, target.getZ() - steve.getZ()).normalize();
            for (int i = 0; i <= 5; i++) {
                BlockPos originPos = BlockPos.containing(steve.position().add(relative3d.scale(i * 2)));
                for (int xOff = -5; xOff <= 5; xOff++) {
                    for (int zOff = -5; zOff <= 5; zOff++) {
                        if (xOff * xOff + zOff * zOff < 25) {
                            BlockPos pos = originPos.offset(xOff, 0, zOff);
                            while (!steve.level().getFluidState(pos).getFluidType().isAir()) {
                                steve.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                                pos = pos.above();
                            }
                        }
                    }
                }
            }
        }
    }
}
