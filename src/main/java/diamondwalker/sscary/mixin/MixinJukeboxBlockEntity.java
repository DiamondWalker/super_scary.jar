package diamondwalker.sscary.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import diamondwalker.sscary.data.server.WorldData;
import diamondwalker.sscary.script.CorruptedMusicDiscScript;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(JukeboxBlockEntity.class)
public abstract class MixinJukeboxBlockEntity extends BlockEntity {

    public MixinJukeboxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "setTheItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/JukeboxSongPlayer;play(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/Holder;)V", shift = At.Shift.AFTER))
    private void playingDisc(ItemStack item, CallbackInfo ci) {
        if (level != null && level.getServer() != null) {
            WorldData data = WorldData.get(level.getServer());
            if (data.progression.hasBeenAngered()) {
                data.newScripts.startScript(new CorruptedMusicDiscScript(level, getBlockPos(), item.getItem()));
            }
        }
    }
}
