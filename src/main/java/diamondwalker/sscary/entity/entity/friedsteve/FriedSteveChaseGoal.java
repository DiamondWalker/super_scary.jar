package diamondwalker.sscary.entity.entity.friedsteve;

import diamondwalker.sscary.ai.ImprovedMeleeAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
    public boolean canContinueToUse() {
        return super.canContinueToUse() && steve.getState() == EnumFriedSteveState.CHASING;
    }

    @Override
    public void tick() {
        super.tick();

        if (steve.isInFluidType()) {
            LivingEntity target = steve.getTarget();
            Vec3 relative3d = new Vec3(target.getX() - steve.getX(), 0, target.getZ() - steve.getZ()).normalize();
            ServerLevel level = (ServerLevel) steve.level();
            boolean boiled = false;
            for (int i = 0; i <= 5; i++) {
                BlockPos originPos = BlockPos.containing(steve.position().add(relative3d.scale(i * 2)));
                for (int xOff = -5; xOff <= 5; xOff++) {
                    for (int zOff = -5; zOff <= 5; zOff++) {
                        if (xOff * xOff + zOff * zOff < 25) {
                            BlockPos pos = originPos.offset(xOff, 0, zOff);
                            while (!level.getFluidState(pos).getFluidType().isAir()) {
                                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                                level.sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX(), pos.getY(), pos.getZ(), 1, 1, 1, 1, 0.01);
                                pos = pos.above();
                                boiled = true;
                            }
                        }
                    }
                }
            }
            if (boiled) level.playSound(null, steve.blockPosition(), SoundEvents.GENERIC_BURN, SoundSource.BLOCKS, 3.0f, 1.0f);
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (canPerformAttack(target)) {
            steve.setState(EnumFriedSteveState.CAUGHT);
        }
    }
}
